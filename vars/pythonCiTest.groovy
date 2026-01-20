//vars/pythonCiTest.groovy

def call() {

    stage('FastAPI Import Test') {
        pythonImportCheck()
    }

    if (env.IS_PR == 'true') {
        stage('PR Checks') {
            pythonPrTests()
            pythonArchiveCoverage()
            pythonRecordCoverage(true)
        }
    }

    if (env.BRANCH_NAME == 'main' && env.IS_PR == 'false') {
        stage('Main Branch Tests & Coverage') {
            pythonMainTests()
            pythonArchiveCoverage()
            pythonRecordCoverage(false)
        }

        stage('Test Report') {
            junit 'reports/junit.xml'
        }
    }

    if (env.IS_PR == 'true') {
        stage('Comment Coverage to PR') {
            githubCommentCoverage()
        }
    }
}
