import org.company.domain.*
import org.company.policy.*
import org.company.guard.*

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
