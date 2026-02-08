package org.company.failure.ast

import java.lang.annotation.*

import org.codehaus.groovy.transform.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.syntax.Token

import org.company.domain.FailureType
import org.company.context.FailureContext
import org.company.guard.FailableGuard

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass("org.company.failure.ast.FailureHandledAST")
@interface FailureHandled {
    FailureType type()
    String stage()
    String message()
    boolean unstable() default false
}

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class FailureHandledAST implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        if (!nodes || nodes.length < 2) return
        if (!(nodes[0] instanceof AnnotationNode)) return
        if (!(nodes[1] instanceof MethodNode)) return

        AnnotationNode ann = (AnnotationNode) nodes[0]
        MethodNode method = (MethodNode) nodes[1]

        if (!(method.code instanceof BlockStatement)) return

        injectGuard(method, ann)
    }

    private void injectGuard(MethodNode method, AnnotationNode ann) {
        BlockStatement original = (BlockStatement) method.code

        // script 파라미터가 있으면 그걸 사용, 없으면 this 사용(권장: script 파라미터 쓰기)
        Expression scriptExpr = resolveScriptExpr(method)

        // FailureContext(type, stage, message, unstable)
        Expression ctxExpr = new ConstructorCallExpression(
            ClassHelper.make(FailureContext),
            new ArgumentListExpression(
                ann.members['type'],
                ann.members['stage'],
                ann.members['message'],
                ann.members['unstable']
            )
        )

        // def __fhCtx = FailableGuard.begin(script, new FailureContext(...))
        VariableExpression ctxVar = new VariableExpression("__fhCtx")
        Expression declareCtxExpr = new DeclarationExpression(
            ctxVar,
            Token.newSymbol("=", -1, -1),
            beginCall
        )

        Statement declareCtxStmt = new ExpressionStatement(declareCtxExpr)


        // catch(Throwable t) { FailableGuard.fail(script, __fhCtx, t) }
        VariableExpression tVar = new VariableExpression("t")
        Statement failStmt = new ExpressionStatement(
            new MethodCallExpression(
                new ClassExpression(ClassHelper.make(FailableGuard)),
                "fail",
                new ArgumentListExpression(scriptExpr, ctxVar, tVar)
            )
        )

        CatchStatement catchStmt = new CatchStatement(
            new Parameter(ClassHelper.make(Throwable), "t"),
            new BlockStatement([failStmt], new VariableScope())
        )

        // finally { FailableGuard.end(script, __fhCtx) }
        Statement endStmt = new ExpressionStatement(
            new MethodCallExpression(
                new ClassExpression(ClassHelper.make(FailableGuard)),
                "end",
                new ArgumentListExpression(scriptExpr, ctxVar)
            )
        )

        BlockStatement finallyBlock = new BlockStatement([endStmt], new VariableScope())

        TryCatchStatement tryCatch = new TryCatchStatement(
            original,        // try block = 원래 본문 그대로
            finallyBlock,    // finally
            [catchStmt]
        )

        method.code = new BlockStatement(
            [declareCtx, tryCatch],
            new VariableScope()
        )
    }

    private Expression resolveScriptExpr(MethodNode method) {
        def hasScriptParam = method.parameters?.any { it.name == "script" }
        if (hasScriptParam) return new VariableExpression("script")
        return new VariableExpression("this")
    }
}
