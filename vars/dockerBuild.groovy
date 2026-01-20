//dockerBuild.groovy
import org.company.ci.RetryPolicy

def call(Map args = [:]) {
  def image = args.image
  def tag   = args.get('tag', 'latest')

  if (!image) {
    error("dockerBuild: image is required")
  }

  withCredentials([usernamePassword(
          credentialsId: 'docker-pat',
          usernameVariable: 'DOCKER_USER',
          passwordVariable: 'DOCKER_PASS'
        )]) {
    sh '''
      echo "$DOCKER_PASS" | docker login \
        -u "$DOCKER_USER" --password-stdin
    '''

    def buildCode = sh(
      script: "docker build -t ${image}:${tag} .",
      returnStatus: true
    )

    if (buildCode != 0) {
      error("docker build failed")
    }

    def retries = RetryPolicy.retryCount("INFRA") // 예시

    retry(retries) {
      sh "docker push ${image}:${tag}"
    }
  }
}