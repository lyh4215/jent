package org.company.features.stageAllowable

class NotPullRequestPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.IS_PR != 'true'
    }
}
