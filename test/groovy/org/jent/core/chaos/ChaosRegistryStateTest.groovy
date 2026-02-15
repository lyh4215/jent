package org.jent.core.chaos

import org.junit.Test

class ChaosRegistryStateTest {

    @Test
    void getReturnsSameRegistryForSameRawBuild() {
        def rawBuild = new Object()
        def script = new Expando(currentBuild: new Expando(rawBuild: rawBuild))

        def first = ChaosRegistryState.get(script)
        def second = ChaosRegistryState.get(script)

        assert first.is(second)
    }

    @Test
    void getReturnsDifferentRegistryForDifferentRawBuild() {
        def scriptA = new Expando(currentBuild: new Expando(rawBuild: new Object()))
        def scriptB = new Expando(currentBuild: new Expando(rawBuild: new Object()))

        def first = ChaosRegistryState.get(scriptA)
        def second = ChaosRegistryState.get(scriptB)

        assert !first.is(second)
    }

    @Test
    void getFallsBackToScriptInstanceWhenRawBuildIsMissing() {
        def script = new Expando()

        def first = ChaosRegistryState.get(script)
        def second = ChaosRegistryState.get(script)
        def third = ChaosRegistryState.get(new Expando())

        assert first.is(second)
        assert !first.is(third)
    }

    @Test
    void currentBuildRefFormatsRunIdentity() {
        def run = new Expando(number: 12, parent: new Expando(fullName: 'folder/job'))
        def script = new Expando(currentBuild: new Expando(rawBuild: run))

        assert ChaosRegistryState.currentBuildRef(script) == 'folder/job#12'
        assert ChaosRegistryState.currentBuildRef(new Expando()) == 'local-script'
    }
}
