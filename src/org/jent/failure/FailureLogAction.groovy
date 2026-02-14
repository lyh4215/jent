package org.jent.failure

import org.jent.core.failure.FailureAction
import org.jent.core.failure.FailureContext

class FailureLogAction implements FailureAction {

    @Override
    void execute(def script, FailureContext ctx) {
        script.echo "[LOG] failedAt=${ctx.stageId}"
        script.currentBuild.description = "Failed at stage: ${ctx.stageId}"
    }
}
