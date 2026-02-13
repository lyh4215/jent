import org.company.core.failure.FailureRegistry

// stage-specific
def call(String stageId, Object action) {
    def instance = resolveAction(action)
    FailureRegistry.addFailureHandler(this, stageId, instance)
}

// global
def call(Object action) {
    def instance = resolveAction(action)
    FailureRegistry.addGlobalFailureHandler(this, instance)
}

// 내부 유틸
private def resolveAction(Object action) {
    if (action instanceof Class) {
        return action.newInstance()
    }
    return action
}
