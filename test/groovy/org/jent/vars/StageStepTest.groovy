package org.jent.vars

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.jent.when.ParamFlagPolicy
import org.junit.Before
import org.junit.Test

class StageStepTest extends BasePipelineTest {

    def stageScript

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()

        helper.scriptRoots += '.'

        helper.registerAllowedMethod('stage', [String, Closure]) { String name, Closure body ->
            body.call()
        }
        helper.registerAllowedMethod('retry', [Integer, Closure]) { Integer count, Closure body ->
            body.call()
        }
        helper.registerAllowedMethod('catchError', [Map, Closure]) { Map opts, Closure body ->
            try {
                body.call()
            } catch (Exception ignored) {
                // emulate Jenkins catchError behavior: mark result but continue
            }
        }
        helper.registerAllowedMethod('error', [String]) { String message ->
            throw new RuntimeException(message)
        }
        helper.registerAllowedMethod('echo', [String]) { String message -> }

        stageScript = loadScript('vars/Stage.groovy')
    }

    @Test
    void "Stage test executes body and completes"() {
        boolean executed = false
        stageScript.call('test') {
            executed = true
            stageScript.echo 'hello from test stage'
        }

        assertJobStatusSuccess()
        assert executed
        assert helper.callStack.find { call ->
            call.methodName == 'stage' && call.argsToString().contains('test')
        }
        assert helper.callStack.find { call ->
            call.methodName == 'echo' && call.argsToString().contains('hello from test stage')
        }
    }

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

        assertJobStatusSuccess()
        assert executedA
        assert !executedB
        assert helper.callStack.find { call ->
            call.methodName == 'catchError' && call.argsToString().contains('NOT_BUILT')
        }
    }
}
