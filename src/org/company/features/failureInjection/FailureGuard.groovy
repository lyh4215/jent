package org.company.features.failureInjection

import org.company.domain.*
import org.company.policy.*

class FailureGuard implements Serializable {

  private final def script
  private final FailurePolicy policy

  FailureGuard(script, FailurePolicy policy) {
    this.script = script
    this.policy = policy
  }

  void around(InjectionPoint point, Closure body) {
    def decision = policy.decide(point)

    switch (decision) {
      case FailureAction.FAIL:
        script.error("Failure injected at ${point}")
        return

      case FailureAction.UNSTABLE:
        script.unstable("Unstable injected at ${point}")
        // UNSTABLE는 실행은 시킨다
        body()
        return

      case FailureAction.SKIP:
        body()
        return
    }
  }
}
