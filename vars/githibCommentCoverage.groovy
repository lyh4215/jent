// vars/githubCommentCoverage.groovy
import org.jent.ci.CoverageParser

def call() {
    def report = CoverageParser.extractLineRate('coverage.xml')

    withCredentials([
        usernamePassword(
            credentialsId: 'jenkins-ci-app',
            passwordVariable: 'GITHUB_TOKEN',
            usernameVariable: 'GITHUB_APP_USER'
        )
    ]) {
        sh """
        curl -s -H "Authorization: token $GITHUB_TOKEN" \
            -H "Content-Type: application/json" \
            -X POST \
            -d '{"body":"### ✅ Coverage Report\\n\\n```\\n${report}\\n```"}' \
            "https://api.github.com/repos/lyh4215/jenkins-study/issues/${env.CHANGE_ID}/comments"
        """
    }
}
