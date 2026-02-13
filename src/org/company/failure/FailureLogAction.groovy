package org.company.failure

import org.company.core.failure.FailureAction
import org.company.core.failure.FailureContext

class FailureLogAction implements FailureAction {

    @Override
    void execute(def script, FailureContext ctx) {
        script.echo "[LOG] failedAt=${ctx.stageId}"
        script.currentBuild.description = "Failed at stage: ${ctx.stageId}"
    }
}
