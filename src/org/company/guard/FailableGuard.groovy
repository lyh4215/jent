package org.company.guard

import org.company.context.FailureContext
import org.company.util.Failure

class FailableGuard {

    static void run(
        def script,
        FailureContext baseContext,
        Closure body
    ) {
        try {
            body.call()
        } catch (Exception e) {
            FailureContext ctx = baseContext.withException(e)
            Failure.fail(script, ctx)
        }
    }
}
