package org.jent.vars

import org.jent.core.failure.FailureAction
import org.jent.core.failure.FailureContext
import org.jent.failure.FailureLogAction
import org.junit.Test

class StageWithFailureTest extends BaseVarsPipelineTest {

    @Test
    void onFailureActionIsInvokedWhenStageBodyFails() {
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

    @Test
    void sharedGlobalOnFailureActionRunsForBothFailedParallelStages() {
        def invocations = []
        FailureAction action = new FailureAction() {
            @Override
            void execute(def script, FailureContext ctx) {
                invocations << ctx.stageId
            }
        }

        onFailureScript.call(action)

        try {
            stageScript.parallel(
                    left: {
                        stageScript.call('left') { throw new RuntimeException('left failed') }
                    },
                    right: {
                        stageScript.call('right') { throw new RuntimeException('right failed') }
                    }
            )
            assert false: 'parallel should fail when branches fail'
        } catch (RuntimeException ignored) {
            // expected
        }

        assert invocations.size() == 2
        assert invocations.contains('left')
        assert invocations.contains('right')
    }
}
