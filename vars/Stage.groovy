import org.company.when.WhenPolicy

def call(String id, Map opts = [:], Closure body) {

    boolean skip = false

    if (opts.when) {
        def policy = resolvePolicy(opts.when)
        skip = !policy.allows(this)
    }

    stage(id) {

        if (skip) {
            echo "⏭ Skipped by When"
            return
        }

        try {
            body.call()
        } catch (Exception e) {
            FailureRegistry.execute(id, this, e)
            throw e
        }
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
