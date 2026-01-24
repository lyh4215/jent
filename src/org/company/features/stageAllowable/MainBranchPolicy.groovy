package org.company.features.stageAllowable
class MainBranchPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.BRANCH_NAME == 'main'
    }
}
