package org.company.features.stageAllowable

import org.company.policy.StagePolicy

class CiEnabledPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.SKIP_CI != 'true'
    }
}
