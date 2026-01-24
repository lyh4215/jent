package org.company.features.stageAllowable

import org.company.policy.StagePolicy

class MainBranchPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.BRANCH_NAME == 'main'
    }
}
