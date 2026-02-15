package org.jent.chaos

import org.jent.core.chaos.ChaosException
import org.junit.Test

class EnvChaosPolicyTest {

    @Test
    void matchesReturnsTrueWhenFailAtEqualsPointId() {
        def policy = new EnvChaosPolicy()
        def script = new Expando(env: [FAIL_AT: 'deploy'])

        assert policy.matches('deploy', script)
        assert !policy.matches('build', script)
    }

    @Test
    void failThrowsChaosException() {
        def policy = new EnvChaosPolicy()

        try {
            policy.fail(new Expando(), 'deploy')
            assert false: 'should throw ChaosException'
        } catch (ChaosException e) {
            assert e.message.contains('deploy')
        }
    }
}
