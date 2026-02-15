package org.jent.vars

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before

abstract class BaseVarsPipelineTest extends BasePipelineTest {

    def stageScript
    def whenScript
    def onFailureScript
    def registerChaosScript
    def chaosScript

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
        helper.registerAllowedMethod('parallel', [Map]) { Map branches ->
            def errors = []
            branches.each { _, Closure branch ->
                try {
                    branch.call()
                } catch (Exception e) {
                    errors << e
                }
            }
            if (!errors.isEmpty()) {
                throw errors[0]
            }
        }

        stageScript = loadScript('vars/Stage.groovy')
        whenScript = loadScript('vars/When.groovy')
        onFailureScript = loadScript('vars/OnFailure.groovy')
        registerChaosScript = loadScript('vars/RegisterChaos.groovy')
        chaosScript = loadScript('vars/Chaos.groovy')

        // Ensure every vars script resolves the same build/run key in registry states.
        def scripts = [stageScript, whenScript, onFailureScript, registerChaosScript, chaosScript]
        scripts.each { s ->
            def sharedParams = binding.getVariable('params')
            def sharedEnv = binding.getVariable('env')
            def sharedCurrentBuild = binding.getVariable('currentBuild')

            s.binding.setVariable('params', sharedParams)
            s.binding.setVariable('env', sharedEnv)
            s.binding.setVariable('currentBuild', sharedCurrentBuild)

            // Force direct script-property alignment to keep registry run-key resolution consistent.
            s.setProperty('params', sharedParams)
            s.setProperty('env', sharedEnv)
            s.setProperty('currentBuild', sharedCurrentBuild)
            s.metaClass.getCurrentBuild = { -> sharedCurrentBuild }
        }
    }
}
