// src/org/company/features/stageAllowable/AndPolicy.groovy
package org.company.features.stageAllowable

class AndPolicy implements StagePolicy {

    List<StagePolicy> policies

    AndPolicy(List<StagePolicy> policies) {
        this.policies = policies
    }

    @Override
    boolean isAllowed(def script) {
        policies.every { it.isAllowed(script) }
    }

    @Override
    String reason() {
        policies.collect { it.reason() }.join(" AND ")
    }
}
