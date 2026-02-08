package org.company.features.stageAllowable.conditions

import org.company.features.stageAllowable.StagePolicy

class PreviewPolicy extends StagePolicy {
    @Override
    boolean isAllowed(script) {
        return script.env.IS_PREVIEW == 'true'
    }

    @Override
    String reason() {
        return "Not in preview mode"
    }
}
