package org.company.failure

import org.company.core.failure.FailureAction
import org.company.core.failure.FailureContext

class NotifySlack implements FailureAction {

    @Override
    void execute(def script, FailureContext ctx) {
        script.echo "🔔 Slack notified for stage ${ctx.stageId}"
    }
}
