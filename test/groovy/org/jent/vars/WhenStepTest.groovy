package org.jent.vars

import org.jent.core.when.AndPolicy
import org.jent.core.when.NotPolicy
import org.jent.core.when.OrPolicy
import org.jent.when.BranchPatternPolicy
import org.junit.Test

class WhenStepTest extends BaseVarsPipelineTest {

    @Test
    void whenOfResolvesPolicyClassAndInstance() {
        def operation = whenScript.call()

        def fromClass = operation.of(BranchPatternPolicy)
        def fromInstance = operation.of(new BranchPatternPolicy(patterns: ['main']))

        assert fromClass instanceof BranchPatternPolicy
        assert fromInstance instanceof BranchPatternPolicy
    }

    @Test
    void whenOperatorsCreateOperatorPolicies() {
        def operation = whenScript.call()

        def andPolicy = operation.and([new BranchPatternPolicy(patterns: ['main'])])
        def orPolicy = operation.or([new BranchPatternPolicy(patterns: ['main'])])
        def notPolicy = operation.not(new BranchPatternPolicy(patterns: ['dev']))

        assert andPolicy instanceof AndPolicy
        assert orPolicy instanceof OrPolicy
        assert notPolicy instanceof NotPolicy
    }

    @Test
    void whenAndOrRejectEmptyPolicyList() {
        def operation = whenScript.call()

        try {
            operation.and([]).allows(new Expando(env: [BRANCH_NAME: 'main']))
            assert false: 'and([]) should throw'
        } catch (IllegalArgumentException ignored) {
            // expected
        }

        try {
            operation.or([]).allows(new Expando(env: [BRANCH_NAME: 'main']))
            assert false: 'or([]) should throw'
        } catch (IllegalArgumentException ignored) {
            // expected
        }
    }

    @Test
    void whenNotRejectsNullPolicy() {
        def operation = whenScript.call()

        try {
            operation.not(null).allows(new Expando(env: [BRANCH_NAME: 'main']))
            assert false: 'not(null) should throw'
        } catch (IllegalArgumentException ignored) {
            // expected
        }
    }
}
