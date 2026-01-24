package org.company.features.failureInjection

import org.company.domain.*

class ParamFailurePolicy implements FailurePolicy {

  private final InjectionPoint target
  private final FailureAction action

  ParamFailurePolicy(InjectionPoint target, FailureAction action) {
    this.target = target
    this.action = action
  }

  FailureAction decide(InjectionPoint point) {
    if (target == InjectionPoint.NONE) {
      return FailureAction.SKIP
    }
    return point == target ? action : FailureAction.SKIP
  }
}
