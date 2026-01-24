import org.company.features.stageAllowable.*
import org.company.domain.FailureType
import org.company.features.failureInjection.*

def call() {
    StageGate.stageIfAllowed(this, 'Build & Push Production Image', stagePolicies.build()) {
        withFailureGuard(InjectionPoint.DOCKER_BUILD) {
            dockerBuild(
                image: "lyh4215/jenkins-study-app",
                tag: env.IMAGE_TAG
            )
        }
    }

    StageGate.stageIfAllowed(this, 'Build & Push Preview Image', stagePolicies.previewBuild()) {
        withFailureGuard(InjectionPoint.DOCKER_BUILD) {
            dockerBuild(
                image: "lyh4215/jenkins-study-app",
                tag: env.IMAGE_TAG
            )
        }
    }
}