package org.jent.core.chaos

import org.junit.Test

class ChaosRegistryTest {

    @Test
    void maybeFailDoesNothingWhenNoPolicyMatches() {
        def runtimeScript = new Expando(currentBuild: new Expando(rawBuild: new Object()))
        def policy = [
                matches: { String pointId, def script -> false },
                fail: { def script, String pointId -> throw new RuntimeException('should not fail') }
        ] as ChaosPolicy
        ChaosRegistry.register(runtimeScript, policy)

        ChaosRegistry.maybeFail(runtimeScript, 'deploy')
    }

    @Test
    void maybeFailInvokesFailWhenPolicyMatches() {
        def runtimeScript = new Expando(currentBuild: new Expando(rawBuild: new Object()))
        def policy = [
                matches: { String pointId, def script -> pointId == 'deploy' },
                fail: { def script, String pointId -> throw new ChaosException("boom at ${pointId}") }
        ] as ChaosPolicy
        ChaosRegistry.register(runtimeScript, policy)

        try {
            ChaosRegistry.maybeFail(runtimeScript, 'deploy')
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

        ChaosRegistry.register(script, policy)

        assert logs.any { it.contains('registered policy') }
    }
}
