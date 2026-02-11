package org.company.failure

class FailureLogAction implements FailureAction {

    @Override
    void execute(def script, FailureContext ctx) {
        script.echo "[LOG] failedAt=${ctx.stageId}"
    }
}
