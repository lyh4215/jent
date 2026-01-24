package org.company.features.stageAllowable

import org.company.policy.StagePolicy

class PreviewPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.IS_PREVIEW == 'true'
    }
}
