package org.jent.core.when

import org.junit.Test

class WhenOperationTest {

    @Test
    void operationBuildsComposablePolicies() {
        def op = new WhenOperation()

        def andPolicy = op.and([new AlwaysTruePolicy(), new AlwaysTruePolicy()])
        def orPolicy = op.or([new AlwaysFalsePolicy(), new AlwaysTruePolicy()])
        def notPolicy = op.not(new AlwaysFalsePolicy())

        assert andPolicy.allows(new Expando())
        assert orPolicy.allows(new Expando())
        assert notPolicy.allows(new Expando())
    }

    @Test
    void ofDelegatesToResolver() {
        def op = new WhenOperation()
        def policy = op.of(AlwaysTruePolicy)

        assert policy instanceof AlwaysTruePolicy
        assert policy.allows(new Expando())
    }

    static class AlwaysTruePolicy implements WhenPolicy {
        @Override
        boolean allows(def script) {
            return true
        }
    }

    static class AlwaysFalsePolicy implements WhenPolicy {
        @Override
        boolean allows(def script) {
            return false
        }
    }
}
