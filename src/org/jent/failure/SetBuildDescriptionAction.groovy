package org.jent.failure

import org.jent.core.failure.FailureAction
import org.jent.core.failure.FailureContext

class SetBuildDescriptionAction implements FailureAction {

    boolean includeException = true
    int maxMessageLength = 120

    @Override
    void execute(def script, FailureContext ctx) {
        String base = "Failed at stage: ${ctx.stageId}"
        if (!includeException || ctx?.exception == null) {
            script.currentBuild.description = base
            return
        }

        String message = ctx.exception.message ?: ctx.exception.class.simpleName
        String normalized = message.replaceAll(/[\r\n]+/, " ").trim()
        if (normalized.length() > maxMessageLength) {
            normalized = normalized.substring(0, maxMessageLength) + "..."
        }

        script.currentBuild.description = "${base} (${normalized})"
    }
}
