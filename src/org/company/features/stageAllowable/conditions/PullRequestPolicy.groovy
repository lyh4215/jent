package org.company.features.stageAllowable.conditions

import org.company.features.stageAllowable.StagePolicy

class PullRequestPolicy extends StagePolicy {
    @Override
    boolean isAllowed(script) {
        return script.env.IS_PR == 'true'
    }

    @Override
    String reason() {
        return "This is not a pull request"
    }
}
