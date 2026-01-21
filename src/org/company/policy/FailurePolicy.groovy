package org.company.policy

class FailurePolicy implements Serializable {
  def script

  FailurePolicy(script) {
    this.script = script
  }

    static List<String> choices() {
        return InjectionPoint.values()*.name()
    }


  void maybeFail(InjectionPoint point) {
    def target = script.params.FAIL_AT ?: 'NONE'
    if (target == point.name()) {
      script.env.FAILURE_POINT = point.name()
      script.error("Injected failure at ${point}")
    }
  }
}
