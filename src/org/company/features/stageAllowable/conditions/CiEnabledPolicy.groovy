package org.company.features.stageAllowable

class CiEnabledPolicy extends StagePolicy {
    @Override
    boolean isAllowed(script) {
        return script.env.SKIP_CI != 'true'
    }
    @Override
    String reason() {
        return "CI is disabled"
    }
}
