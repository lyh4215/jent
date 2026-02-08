package org.company.util
import org.company.domain.FailureType
import org.company.context.FailureContext

class Failure {

    static void fail(def script, FailureContext ctx) {
        script.echo "[failed] echo"

        script.currentBuild.description = """
type=${ctx.type}
stage=${ctx.stage}
retryable=${ctx.retryable}
reason=${ctx.reason}
""".stripIndent().trim()

        script.error(ctx.reason)
    }
}
