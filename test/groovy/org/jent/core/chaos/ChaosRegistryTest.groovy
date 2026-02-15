package org.jent.core.chaos

import org.junit.Test

class ChaosRegistryTest {

    @Test
    void maybeFailDoesNothingWhenNoPolicyMatches() {
        def policy = [
                matches: { String pointId, def script -> false },
                fail: { def script, String pointId -> throw new RuntimeException('should not fail') }
        ] as ChaosPolicy
        def registry = new ChaosRegistry()
        registry.register(policy)

        registry.maybeFail(new Expando(), 'deploy')
    }

    @Test
    void maybeFailInvokesFailWhenPolicyMatches() {
        def policy = [
                matches: { String pointId, def script -> pointId == 'deploy' },
                fail: { def script, String pointId -> throw new ChaosException("boom at ${pointId}") }
        ] as ChaosPolicy
        def registry = new ChaosRegistry()
        registry.register(policy)

        try {
            registry.maybeFail(new Expando(), 'deploy')
            assert false: 'should throw ChaosException'
        } catch (ChaosException e) {
            assert e.message.contains('deploy')
        }
    }

    @Test
    void registerLogsWhenVerboseIsEnabled() {
        def logs = []
        def script = new Expando(
                env: [VERBOSE: true],
                echo: { String msg -> logs << msg }
        )
        def policy = [
                matches: { String pointId, def s -> false },
                fail: { def s, String pointId -> }
        ] as ChaosPolicy
        def registry = new ChaosRegistry()

        registry.register(script, policy)

        assert logs.any { it.contains('registered policy') }
    }
}
