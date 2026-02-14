package org.jent.vars

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before

abstract class BaseVarsPipelineTest extends BasePipelineTest {

    def stageScript
    def whenScript
    def onFailureScript
    def registerChaosScript
    def chaosScript
    def chaosHolderScript

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()

        helper.scriptRoots += '.'
        binding.setVariable('params', [:])
        binding.setVariable('env', [:])
        binding.setVariable('currentBuild', new Expando(rawBuild: new Object(), description: null))

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
        helper.registerAllowedMethod('ChaosRegistryHolder', []) { ->
            return chaosHolderScript.call()
        }

        stageScript = loadScript('vars/Stage.groovy')
        whenScript = loadScript('vars/When.groovy')
        onFailureScript = loadScript('vars/OnFailure.groovy')
        registerChaosScript = loadScript('vars/RegisterChaos.groovy')
        chaosScript = loadScript('vars/Chaos.groovy')
        chaosHolderScript = loadScript('vars/ChaosRegistryHolder.groovy')
    }
}
