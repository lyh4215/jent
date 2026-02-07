package org.company.features.stageAllowable

class NotPullRequestPolicy extends StagePolicy {
    @Override
    boolean isAllowed(script) {
        return script.env.IS_PR != 'true'
    }

    @Override
    String reason() {
        return "This is a pull request"
    }
}
