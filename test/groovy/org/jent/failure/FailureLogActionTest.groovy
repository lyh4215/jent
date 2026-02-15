package org.jent.failure

import org.jent.core.failure.FailureContext
import org.junit.Test

class FailureLogActionTest {

    @Test
    void executeWritesLogAndBuildDescription() {
        def echoes = []
        def script = new Expando(
                currentBuild: new Expando(description: null),
                echo: { String msg -> echoes << msg }
        )

        def action = new FailureLogAction()
        def ctx = new FailureContext(stageId: 'deploy', exception: new RuntimeException('boom'))

        action.execute(script, ctx)

        assert echoes == ['[LOG] failedAt=deploy']
        assert script.currentBuild.description == 'Failed at stage: deploy'
    }
}
