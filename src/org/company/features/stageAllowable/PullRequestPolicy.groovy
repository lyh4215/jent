package org.company.features.stageAllowable

import org.company.policy.StagePolicy

class PullRequestPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.IS_PR == 'true'
    }
}
