import org.jent.core.failure.FailureRegistry
import org.jent.core.when.WhenPolicy
import org.jent.core.chaos.ChaosException

def call(String id, Map opts = [:], Closure body) {

    int retryCount = opts.retry ?: 1

    stage(id) {

        // --- When 처리 ---
        if (opts?.when) {
            def policy = resolvePolicy(opts.when)

            if (!policy.allows(this)) {

                catchError(buildResult: 'SUCCESS', stageResult: 'NOT_BUILT') {
                    error("Skipped by When policy: ${policy.class.simpleName}")
                }

                return
            }
        }
        
        try {
            retry(retryCount) {

                try {
                    body.call()
                } catch (ChaosException ce) {
                    // Chaos는 retry 대상 아님
                    throw ce
                }
            }
        } catch (Exception e) {
            FailureRegistry.execute(this, id, e)
            throw e
        }
    }
}

private WhenPolicy resolvePolicy(Object obj) {
    if (obj instanceof Class) return obj.newInstance()
    if (obj instanceof WhenPolicy) return obj
    throw new IllegalArgumentException("When requires a WhenPolicy class or instance")
}
