import org.company.pipeline.when.WhenPolicy

def call(Object policyObj, Closure body) {

    def policy = resolvePolicy(policyObj)

    if (policy.allows(this)) {
        body.call()
    } else {
        echo "⏭ Skipped by When policy: ${policy.class.simpleName}"
    }
}

private WhenPolicy resolvePolicy(Object obj) {

    if (obj instanceof Class) {
        return obj.newInstance()
    }

    if (obj instanceof WhenPolicy) {
        return obj
    }

    throw new IllegalArgumentException(
        "When requires a WhenPolicy class or instance"
    )
}
