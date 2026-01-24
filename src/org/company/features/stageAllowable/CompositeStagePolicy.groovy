package org.company.features.stageAllowable

import org.company.policy.StagePolicy

class CompositeStagePolicy implements StagePolicy {
    List<StagePolicy> policies

    CompositeStagePolicy(List<StagePolicy> policies) {
        this.policies = policies
    }

    boolean isAllowed(script) {
        for (p in policies) {
            if (!p.isAllowed(script)) {
                return false
            }
        }
        return true
    }
}
