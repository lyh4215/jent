package org.company.failure

class FailureRegistry {

    private static Map<String, List<FailureAction>> handlers = [:]

    static void addFailureHandler(String stageId, FailureAction action) {
        if (!handlers.containsKey(stageId)) {
            handlers[stageId] = []
        }
        handlers[stageId] << action
    }

    static void execute(String stageId, def script, Exception e) {
        def ctx = new FailureContext(stageId: stageId, exception: e)

        def actions = handlers[stageId]
        if (actions == null) {
            return
        }

        for (FailureAction action : actions) {
            action.execute(script, ctx)
        }
    }
}
