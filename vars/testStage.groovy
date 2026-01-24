import org.company.features.stageAllowable.*
import org.company.domain.FailureType
import org.company.features.failureInjection.*

def call() {
    StageGate.stageIfAllowed(this, 'Test', stagePolicies.test()) {
        withFailureGuard(InjectionPoint.TEST) {
            pythonCiTest()
        }
    }
}