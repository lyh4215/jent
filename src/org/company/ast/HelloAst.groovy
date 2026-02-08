package org.company.ast

import java.lang.annotation.*
import org.codehaus.groovy.transform.GroovyASTTransformationClass

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass(["org.company.ast.HelloAstTransformation"])
@interface HelloAst {
}
