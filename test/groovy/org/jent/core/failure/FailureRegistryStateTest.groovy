package org.jent.core.failure

import org.junit.Test

class FailureRegistryStateTest {

    @Test
    void getReturnsSameStateForSameRawBuild() {
        def rawBuild = new Object()
        def script = new Expando(currentBuild: new Expando(rawBuild: rawBuild))

        def first = FailureRegistryState.get(script)
        def second = FailureRegistryState.get(script)

        assert first.is(second)
    }

    @Test
    void getReturnsDifferentStateForDifferentRuns() {
        def scriptA = new Expando(currentBuild: new Expando(rawBuild: new Object()))
        def scriptB = new Expando(currentBuild: new Expando(rawBuild: new Object()))

        def first = FailureRegistryState.get(scriptA)
        def second = FailureRegistryState.get(scriptB)

        assert !first.is(second)
    }

    @Test
    void getFallsBackToScriptWhenRawBuildMissing() {
        def script = new Expando()
        def same = script
        def other = new Expando()

        assert FailureRegistryState.get(script).is(FailureRegistryState.get(same))
        assert !FailureRegistryState.get(script).is(FailureRegistryState.get(other))
    }
}
