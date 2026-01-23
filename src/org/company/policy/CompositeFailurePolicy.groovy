// src/org/company/ci/policy/CompositeFailurePolicy.groovy
package org.company.policy

import org.company.domain.*

class CompositeFailurePolicy implements FailurePolicy {

  private final List<FailurePolicy> policies

  CompositeFailurePolicy(List<FailurePolicy> policies) {
    this.policies = policies
  }

  FailureAction decide(InjectionPoint point) {
    policies
      .collect { it.decide(point) }
      .find { it != FailureAction.SKIP }
      ?: FailureAction.SKIP
  }
}
