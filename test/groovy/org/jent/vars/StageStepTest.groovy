package org.jent.vars

import com.lesfurets.jenkins.unit.BasePipelineTest
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
            body.call()
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
}
