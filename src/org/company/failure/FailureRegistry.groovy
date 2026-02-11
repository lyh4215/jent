package org.company.failure

class FailureRegistry {

    private static Map<String, List<FailureAction>> handlers = [:]

    static void addFailureHandler(String stageId, FailureAction action) {
        handlers.computeIfAbsent(stageId) { [] }.add(action)
    }

    static void execute(String stageId, def script, Exception e) {
        def ctx = new FailureContext(stageId: stageId, exception: e)

        handlers.getOrDefault(stageId, []).each { action ->
            action.execute(script, ctx)
        }
    }
}
