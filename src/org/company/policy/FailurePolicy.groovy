// src/org/company/ci/policy/FailurePolicy.groovy
package org.company.policy

import org.company.domain.*

interface FailurePolicy {
  FailureAction decide(InjectionPoint point)
}
