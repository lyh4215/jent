//vars/pythonCiTest.groovy
import org.company.features.stageAllowable.*

def call() {

    stage('FastAPI Import Test') {
        pythonImportCheck()
    }

    StageGate.stageIfAllowed(this, 'PR Checks', [new PullRequestPolicy()]) {
        pythonPrTests()
        pythonArchiveCoverage()
        pythonRecordCoverage(true)
    }

    StageGate.stageIfAllowed(this, 'Main Branch Tests & Coverage', [
            new NotPullRequestPolicy(),
            new MainBranchPolicy()
        ]) {
        pythonMainTests()
        pythonArchiveCoverage()
        pythonRecordCoverage(false)

        junit 'reports/junit.xml'
    }

    StageGate.stageIfAllowed(this, 'Comment Coverage to PR', [new PullRequestPolicy()])  {
        githubCommentCoverage()
    }
}
