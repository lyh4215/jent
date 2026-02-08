package org.company.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.transform.*

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class HelloAstTransformation implements ASTTransformation {
    static {
        println ">>> HelloAstTransformation LOADED"
    }
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        println ">>> HelloAstTransformation visit called"
        if (nodes == null || nodes.length < 2) return

        def annotatedNode = nodes[1]
        if (!(annotatedNode instanceof MethodNode)) return

        MethodNode methodNode = (MethodNode) annotatedNode

        // println "[AST] Hello from <methodName>"
        Statement injected = new ExpressionStatement(
            new MethodCallExpression(
                new PropertyExpression(
                    new VariableExpression("this"),
                    "script"
                ),
                "echo",
                new ArgumentListExpression(
                    new ConstantExpression("[AST] Hello from foo")
                )
            )
        )

        Statement original = methodNode.code
        BlockStatement block = new BlockStatement()
        block.addStatement(injected)

        // methodNode.code가 null일 수도 있으니 방어
        if (original != null) block.addStatement(original)

        methodNode.code = block
    }
}
