// src/org/company/features/stageAllowable/NotPolicy.groovy
package org.company.features.stageAllowable

class NotPolicy implements StagePolicy {

    StagePolicy policy

    NotPolicy(StagePolicy policy) {
        this.policy = policy
    }

    @Override
    boolean isAllowed(def script) {
        !policy.isAllowed(script)
    }

    @Override
    String reason() {
        "NOT (${policy.reason()})"
    }
}
