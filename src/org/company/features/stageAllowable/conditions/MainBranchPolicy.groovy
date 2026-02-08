package org.company.features.stageAllowable.conditions

import org.company.features.stageAllowable.StagePolicy

class MainBranchPolicy extends StagePolicy {

    @Override
    boolean isAllowed(script) {
        return script.env.BRANCH_NAME == 'main'
    }

    @Override
    String reason() {
        return "Not on main branch"
    }
}
