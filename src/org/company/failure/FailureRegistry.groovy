package org.company.failure

class FailureRegistry {

    static void addFailureHandler(def script, String stageId, FailureAction action) {
        def state = FailureRegistryState.get(script)
        if (!state.handlers.containsKey(stageId)) {
            state.handlers[stageId] = []
        }
        state.handlers[stageId] << action
    }

    static void addGlobalFailureHandler(def script, FailureAction action) {
        def state = FailureRegistryState.get(script)
        state.globalHandlers << action
    }

    static void execute(def script, String stageId, Exception e) {
        def state = FailureRegistryState.get(script)
        def ctx = new FailureContext(stageId: stageId, exception: e)

        def actions = state.handlers[stageId]
        if (actions != null) {
            for (FailureAction action : actions) {
                action.execute(script, ctx)
            }
        }
        for (FailureAction action : state.globalHandlers) {
            action.execute(script, ctx)
        }
    }
}
