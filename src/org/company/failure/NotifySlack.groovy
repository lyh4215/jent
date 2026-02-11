package org.company.failure

class NotifySlack implements FailureAction {

    @Override
    void execute(def script, FailureContext ctx) {
        script.echo "🔔 Slack notified for stage ${ctx.stageId}"
    }
}
