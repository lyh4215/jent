@Library('jent@main') _

import org.jent.core.failure.FailureAction
import org.jent.core.failure.FailureContext

// Custom failure action: notify Slack on stage failure
def notifySlackAction = new FailureAction() {
    @Override
    void execute(def script, FailureContext ctx) {
        def msg = "[ALERT] stage=${ctx.stageId}, reason=${ctx.exception?.message ?: ctx.exception?.class?.simpleName}"
        script.slackSend(
                channel: '#ci-alerts',
                color: 'danger',
                message: msg
        )
    }
}

OnFailure(notifySlackAction)

node {
    Stage('test') {
        sh 'make test'
    }

    Stage('deploy') {
        sh 'exit 1'
    }
}
