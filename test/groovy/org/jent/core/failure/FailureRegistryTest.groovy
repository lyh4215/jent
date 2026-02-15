package org.jent.core.failure

import org.junit.Test

class FailureRegistryTest {

    @Test
    void executeRunsStageSpecificAndGlobalHandlers() {
        def calls = []
        def script = new Expando(currentBuild: new Expando(rawBuild: new Object()))
        def ex = new RuntimeException('boom')

        FailureAction stageAction = new FailureAction() {
            @Override
            void execute(def s, FailureContext ctx) {
                calls << "stage:${ctx.stageId}:${ctx.exception.message}"
            }
        }
        FailureAction globalAction = new FailureAction() {
            @Override
            void execute(def s, FailureContext ctx) {
                calls << "global:${ctx.stageId}:${ctx.exception.message}"
            }
        }

        FailureRegistry.addFailureHandler(script, 'deploy', stageAction)
        FailureRegistry.addGlobalFailureHandler(script, globalAction)
        FailureRegistry.execute(script, 'deploy', ex)

        assert calls == ['stage:deploy:boom', 'global:deploy:boom']
    }

    @Test
    void executeRunsOnlyGlobalWhenStageHandlerMissing() {
        def calls = []
        def script = new Expando(currentBuild: new Expando(rawBuild: new Object()))

        FailureAction globalAction = new FailureAction() {
            @Override
            void execute(def s, FailureContext ctx) {
                calls << ctx.stageId
            }
        }

        FailureRegistry.addGlobalFailureHandler(script, globalAction)
        FailureRegistry.execute(script, 'build', new RuntimeException('x'))

        assert calls == ['build']
    }

    @Test
    void addFailureHandlerAppendsMultipleActionsForSameStage() {
        def script = new Expando(currentBuild: new Expando(rawBuild: new Object()))
        def state = FailureRegistryState.get(script)

        FailureAction a1 = { def s, FailureContext ctx -> } as FailureAction
        FailureAction a2 = { def s, FailureContext ctx -> } as FailureAction

        FailureRegistry.addFailureHandler(script, 'test', a1)
        FailureRegistry.addFailureHandler(script, 'test', a2)

        assert state.handlers['test'].size() == 2
    }
}
