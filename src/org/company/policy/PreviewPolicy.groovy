package org.company.policy

class PreviewPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.IS_PREVIEW == 'true'
    }
}
