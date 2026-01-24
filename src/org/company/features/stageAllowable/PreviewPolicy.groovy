package org.company.features.stageAllowable

class PreviewPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.IS_PREVIEW == 'true'
    }
}
