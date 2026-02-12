import org.company.when.SkipStageException
import org.company.failure.FailureRegistry
import org.company.when.WhenPolicy

def call(String id, Map opts = [:], Closure body) {

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
            body.call()
        } catch (SkipStageException se) {
            // ✅ 스킵은 실패 훅/실패 처리 대상이 아님
            return
        } catch (Exception e) {
            FailureRegistry.execute(id, this, e)
            throw e
        }
    }
}

private WhenPolicy resolvePolicy(Object obj) {
    if (obj instanceof Class) return obj.newInstance()
    if (obj instanceof WhenPolicy) return obj
    throw new IllegalArgumentException("When requires a WhenPolicy class or instance")
}
