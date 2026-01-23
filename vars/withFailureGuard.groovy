import org.company.domain.*
import org.company.policy.*
import org.company.guard.*

def call(
  InjectionPoint point,
  Closure body
) {
  InjectionPoint failAt =
    InjectionPoint.valueOf(params.FAIL_AT)

  FailureAction action =
    FailureAction.valueOf(params.FAIL_ACTION)

  def policy = new ParamFailurePolicy(failAt, action)
  def guard  = new FailureGuard(this, policy)

  guard.around(point, body)
}
