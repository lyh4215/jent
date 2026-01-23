package org.company.policy

import org.company.policy.StagePolicy

class AllOfPolicy implements StagePolicy {
    List<StagePolicy> policies

    AllOfPolicy(List<StagePolicy> policies) {
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
