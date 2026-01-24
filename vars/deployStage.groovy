import org.company.features.stageAllowable.*
import org.company.domain.FailureType
import org.company.features.failureInjection.*

def call() {
    StageGate.stageIfAllowed(this, 'Deploy', stagePolicies.deploy()) {
        withFailureGuard(InjectionPoint.DEPLOY) {
            updateImageTag() //for argoCD
        }
    }
}