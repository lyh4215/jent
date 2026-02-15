package org.jent.chaos

import org.jent.core.chaos.ChaosException
import org.junit.Test

class ParameterChaosPolicyTest {

    @Test
    void matchesReturnsFalseWhenChaosIsDisabled() {
        def logs = []
        def script = new Expando(
                params: [CHAOS_ENABLED: false],
                env: [VERBOSE: true],
                echo: { String msg -> logs << msg }
        )
        def policy = new ParameterChaosPolicy()

        assert !policy.matches('deploy', script)
        assert logs.any { it.contains('disabled') }
    }

    @Test
    void matchesReturnsFalseWhenNoPointsConfigured() {
        def logs = []
        def script = new Expando(
                params: [CHAOS_ENABLED: true],
                env: [VERBOSE: true],
                echo: { String msg -> logs << msg }
        )
        def policy = new ParameterChaosPolicy()

        assert !policy.matches('deploy', script)
        assert logs.any { it.contains('no CHAOS_POINTS configured') }
    }

    @Test
    void matchesNormalizesPointListAndMatchesCaseInsensitively() {
        def script = new Expando(params: [CHAOS_ENABLED: 'true', CHAOS_POINTS: 'BUILD, deploy  TEST,deploy'])
        def policy = new ParameterChaosPolicy()

        assert policy.matches('deploy', script)
        assert policy.matches('build', script)
        assert !policy.matches('release', script)
    }

    @Test
    void matchesReturnsFalseForEmptyPointId() {
        def logs = []
        def script = new Expando(
                params: [CHAOS_ENABLED: true, CHAOS_POINTS: 'deploy'],
                env: [VERBOSE: 'true'],
                echo: { String msg -> logs << msg }
        )
        def policy = new ParameterChaosPolicy()

        assert !policy.matches('  ', script)
        assert logs.any { it.contains('empty pointId') }
    }

    @Test
    void failThrowsChaosException() {
        def policy = new ParameterChaosPolicy()
        try {
            policy.fail(new Expando(), 'build')
            assert false: 'should throw ChaosException'
        } catch (ChaosException e) {
            assert e.message.contains('build')
        }
    }
}
