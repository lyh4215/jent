package org.jent.failure

import org.jent.core.failure.FailureAction
import org.jent.core.failure.FailureContext

class NotifySlack implements FailureAction {

    @Override
    void execute(def script, FailureContext ctx) {
        script.echo "🔔 Slack notified for stage ${ctx.stageId}"
    }
}
