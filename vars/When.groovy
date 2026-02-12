import org.company.when.WhenPolicy
import org.company.when.SkipStageException

def call(Object policyObj) {
    WhenPolicy policy = resolvePolicy(policyObj)

    if (!policy.allows(this)) {
        // Declarative의 skipped 흉내: stageResult를 NOT_BUILT로 마킹
        catchError(buildResult: 'SUCCESS', stageResult: 'NOT_BUILT') {
            error("Skipped by When policy: ${policy.class.simpleName}")
        }

        // ✅ 여기서 stage body 흐름 자체를 끊어야 함
        throw new SkipStageException("Skip stage by policy")
    }
}

private WhenPolicy resolvePolicy(Object obj) {
    if (obj instanceof Class) return obj.newInstance()
    if (obj instanceof WhenPolicy) return obj
    throw new IllegalArgumentException("When requires a WhenPolicy class or instance")
}
