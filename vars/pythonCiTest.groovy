//vars/pythonCiTest.groovy
import org.company.features.stageAllowable.*
import org.company.domain.FailureType
import org.company.context.*
import org.company.guard.*

def call() {

    stage('FastAPI Import Test') {
        pythonImportCheck()
    }

    StageGate.stageIfAllowed(this, 'PR Checks', new PullRequestPolicy()) {
        FailableGuard.run(this,
            new FailureContext(
                FailureType.TEST,
                'PR Checks',
                'PR Tests Failed',
                false
            )
        ) {
            pythonPrTests()
            pythonArchiveCoverage()
            pythonRecordCoverage(true)
        }
    }

    StageGate.stageIfAllowed(this, 'Main Branch Tests & Coverage', 
        StagePolicy.and(
            new NotPullRequestPolicy(),
            new MainBranchPolicy()
        )) {
        FailableGuard.run(this,
            new FailureContext(
                FailureType.TEST,
                'Main Branch Tests & Coverage',
                'Main Branch Tests Failed',
                false
            )
        ) {
            pythonMainTests()
            pythonArchiveCoverage()
            pythonRecordCoverage(false)

            junit 'reports/junit.xml'
        }
    }

    StageGate.stageIfAllowed(this, 'Comment Coverage to PR', new PullRequestPolicy())  {
        githubCommentCoverage()
    }
}
