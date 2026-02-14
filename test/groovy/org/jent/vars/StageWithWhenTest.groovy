package org.jent.vars

import org.jent.when.BranchPatternPolicy
import org.jent.when.ParamFlagPolicy
import org.junit.Test

class StageWithWhenTest extends BaseVarsPipelineTest {

    @Test
    void "ParamFlagPolicy runs stage A and skips stage B when RUN_STAGE is A"() {
        binding.setVariable('params', [RUN_STAGE: 'A'])

        boolean executedA = false
        boolean executedB = false

        stageScript.call('A', [when: new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'A')]) {
            executedA = true
        }
        stageScript.call('B', [when: new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'B')]) {
            executedB = true
        }

        assert executedA
        assert !executedB
        def catchErrorCall = helper.callStack.find { call -> call.methodName == 'catchError' }
        assert catchErrorCall
        assert catchErrorCall.args[0].stageResult == 'NOT_BUILT'
    }

    @Test
    void "When composition runs A and skips B"() {
        binding.setVariable('params', [RUN_STAGE: 'A'])
        binding.setVariable('env', [BRANCH_NAME: 'main'])

        def whenA = whenScript.call().and([
                new BranchPatternPolicy(patterns: ['main']),
                whenScript.call().not(new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'B'))
        ])
        def whenB = whenScript.call().and([
                new BranchPatternPolicy(patterns: ['main']),
                new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'B')
        ])

        boolean executedA = false
        boolean executedB = false

        stageScript.call('A', [when: whenA]) { executedA = true }
        stageScript.call('B', [when: whenB]) { executedB = true }

        assert executedA
        assert !executedB
    }
}
