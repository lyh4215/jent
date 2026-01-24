package org.company.features.stageAllowable

class CiEnabledPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.SKIP_CI != 'true'
    }

    String reason() {
        return "CI is disabled"
    }
}
