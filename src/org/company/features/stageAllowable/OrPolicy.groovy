// src/org/company/features/stageAllowable/OrPolicy.groovy
package org.company.features.stageAllowable

class OrPolicy implements StagePolicy {

    List<StagePolicy> policies

    OrPolicy(List<StagePolicy> policies) {
        this.policies = policies
    }

    @Override
    boolean isAllowed(def script) {
        policies.any { it.isAllowed(script) }
    }

    @Override
    String reason() {
        policies.collect { it.reason() }.join(" OR ")
    }
}
