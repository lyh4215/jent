import org.jent.domain.*
import org.jent.policy.*
import org.jent.features.failureInjection.*

def call(
  InjectionPoint point,
  Closure body
) {
  InjectionPoint failAt =
    InjectionPoint.from(params.FAIL_AT)

  FailureAction action =
    FailureAction.from(params.FAIL_ACTION)

  def policy = new ParamFailurePolicy(failAt, action)
  def guard  = new FailureGuard(this, policy)

  guard.around(point, body)
}
