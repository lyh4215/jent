package org.company.policy

class FailureGuard implements Serializable {
  def policy

  FailureGuard(policy) {
    this.policy = policy
  }

  def around(InjectionPoint point, Closure body) {
    policy.maybeFail(point)
    body()
  }
}
