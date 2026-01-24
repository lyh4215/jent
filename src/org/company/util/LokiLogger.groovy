package org.company.util

import org.company.context.FailureContext

class LokiLogger implements CiLogger {

    @Override
    void logFailure(def script, FailureContext ctx) {
        def msg = [
            'ci_event=failure',
            "type=${ctx.type}",
            "stage=\"${ctx.stage}\"",
            "retryable=${ctx.retryable}",
            "reason=\"${ctx.reason}\""
        ].join(' ')

        script.echo(msg)
    }
}
