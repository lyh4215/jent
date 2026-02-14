package org.jent.vars

import org.jent.chaos.ParameterChaosPolicy
import org.jent.core.chaos.ChaosException
import org.junit.Test

class StageWithChaosTest extends BaseVarsPipelineTest {

    @Test
    void chaosInjectsFailureOnlyWhenEnabledAndPointMatches() {
        registerChaosScript.call(new ParameterChaosPolicy())

        binding.setVariable('params', [CHAOS_ENABLED: true, CHAOS_POINTS: 'point.a'])
        try {
            chaosScript.call('point.a') {
                assert false: 'body should not run when chaos is injected'
            }
            assert false: 'chaos exception expected'
        } catch (ChaosException e) {
            assert e.message.contains('point.a')
        }

        binding.setVariable('params', [CHAOS_ENABLED: true, CHAOS_POINTS: 'other.point'])
        boolean ran = false
        chaosScript.call('point.a') {
            ran = true
        }
        assert ran
    }
}
