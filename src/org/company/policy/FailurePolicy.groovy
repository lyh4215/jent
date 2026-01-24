// src/org/company/ci/policy/FailurePolicy.groovy
package org.company.policy

import org.company.domain.*
import org.company.features.failureInjection.InjectionPoint

interface FailurePolicy {
  FailureAction decide(InjectionPoint point)
}
