package org.jent.vars

import org.jent.failure.FailureLogAction
import org.junit.Test

class StageWithFailureTest extends BaseVarsPipelineTest {

    @Test
    void "OnFailure action is invoked when stage body fails"() {
        onFailureScript.call('deploy', new FailureLogAction())

        try {
            stageScript.call('deploy') {
                throw new RuntimeException('boom')
            }
            assert false: 'exception should have been re-thrown'
        } catch (RuntimeException e) {
            assert e.message == 'boom'
        }

        assert binding.currentBuild.description == 'Failed at stage: deploy'
        assert helper.callStack.find { call ->
            call.methodName == 'echo' && call.argsToString().contains('[LOG] failedAt=deploy')
        }
    }
}
