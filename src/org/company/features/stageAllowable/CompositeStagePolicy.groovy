package org.company.features.stageAllowable

class CompositeStagePolicy extends StagePolicy {
    List<StagePolicy> policies

    CompositeStagePolicy(List<StagePolicy> policies) {
        this.policies = policies
    }

    @Override
    boolean isAllowed(script) {
        for (p in policies) {
            if (!p.isAllowed(script)) {
                return false
            }
        }
        return true
    }
}
