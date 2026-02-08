package org.company.guard

import org.company.context.FailureContext
import org.company.util.Failure

class FailableGuard {

    static FailureContext begin(def script, FailureContext base) {
        script.echo "[INFO] enter failable guard: ${base.summary()}"
        return base
    }

    static void fail(def script, FailureContext base, Throwable t) {
        script.echo "[INFO] failable guard caught: ${t.getClass().getSimpleName()}"
        Failure.fail(script, base.withException(t))
    }

    static void end(def script, FailureContext base) {
        // 필요하면 마무리(메트릭/로그) 추가
        script.echo "[INFO] exit failable guard: ${base.summary()}"
    }
}
