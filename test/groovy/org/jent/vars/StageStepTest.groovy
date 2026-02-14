package org.jent.vars

import org.junit.Test

class StageStepTest extends BaseVarsPipelineTest {

    @Test
    void stageExecutesBodyAndCompletes() {
        boolean executed = false
        stageScript.call('test') {
            executed = true
            stageScript.echo 'hello from test stage'
        }

        assert executed
        assert helper.callStack.find { call ->
            call.methodName == 'stage' && call.argsToString().contains('test')
        }
        assert helper.callStack.find { call ->
            call.methodName == 'echo' && call.argsToString().contains('hello from test stage')
        }
    }

    @Test
    void duplicateStageIdThrowsIllegalArgumentException() {
        stageScript.call('same-id') { }

        try {
            stageScript.call('same-id') { }
            assert false: 'duplicate stage id should throw'
        } catch (IllegalArgumentException e) {
            assert e.message.toLowerCase().contains('duplicate')
            assert e.message.contains('same-id')
        }
    }
}
