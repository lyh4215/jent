package org.jent.core.failure

import com.cloudbees.groovy.cps.NonCPS

class FailureRegistry {

    static void addFailureHandler(def script, String stageId, FailureAction action) {
        def state = FailureRegistryState.get(script)
        addStageHandler(state, stageId, action)
    }

    static void addGlobalFailureHandler(def script, FailureAction action) {
        def state = FailureRegistryState.get(script)
        addGlobalHandler(state, action)
    }

    static void execute(def script, String stageId, Exception e) {
        def state = FailureRegistryState.get(script)
        def ctx = new FailureContext(stageId: stageId, exception: e)
        def resolved = resolveHandlers(state, stageId)

        def actions = resolved.stageHandlers
        if (actions != null) {
            for (FailureAction action : actions) {
                action.execute(script, ctx)
            }
        }
        for (FailureAction action : resolved.globalHandlers) {
            action.execute(script, ctx)
        }
    }

    @NonCPS
    private static void addStageHandler(FailureRegistryData state, String stageId, FailureAction action) {
        synchronized (state) {
            def actions = state.handlers[stageId]
            if (actions == null) {
                actions = []
                state.handlers[stageId] = actions
            }
            actions << action
        }
    }

    @NonCPS
    private static void addGlobalHandler(FailureRegistryData state, FailureAction action) {
        synchronized (state) {
            state.globalHandlers << action
        }
    }

    @NonCPS
    private static Map resolveHandlers(FailureRegistryData state, String stageId) {
        synchronized (state) {
            List<FailureAction> stageHandlers = state.handlers[stageId] ? new ArrayList<FailureAction>(state.handlers[stageId]) : []
            List<FailureAction> globalHandlers = new ArrayList<FailureAction>(state.globalHandlers)
            return [stageHandlers: stageHandlers, globalHandlers: globalHandlers]
        }
    }
}
