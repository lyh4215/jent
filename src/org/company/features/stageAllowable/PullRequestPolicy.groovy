package org.company.features.stageAllowable

class PullRequestPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.IS_PR == 'true'
    }
}
