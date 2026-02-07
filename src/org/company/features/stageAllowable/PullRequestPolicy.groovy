package org.company.features.stageAllowable

class PullRequestPolicy extends StagePolicy {
    @Override
    boolean isAllowed(script) {
        return script.env.IS_PR == 'true'
    }
}
