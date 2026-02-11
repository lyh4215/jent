import org.company.failure.FailureRegistry

def call(String stageId, Object action) {

    def actionInstance

    if (action instanceof Class) {
        actionInstance = action.newInstance()
    } else {
        actionInstance = action
    }

    FailureRegistry.addFailureHandler(stageId, actionInstance)
}
