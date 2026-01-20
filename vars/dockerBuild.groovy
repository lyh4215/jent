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

//     sh '''
//     set -eux

//     # buildkit 작업 디렉토리
//     mkdir -p ${BUILDKIT_DIR}

//     # buildkitd 실행 (rootless)
//     echo "Starting buildkitd (rootless)..."
//     env | grep -i buildkit || true
//     id

//     buildkitd \
//         --addr unix://${BUILDKIT_SOCK} \
//         --root ${BUILDKIT_DIR} \
//         --oci-worker-no-process-sandbox
//         > /home/jenkins/buildkit/buildkitd.log 2>&1 &


//     for i in $(seq 1 10); do
//     if buildctl --addr ${BUILDKIT_SOCK} debug workers >/dev/null 2>&1; then
//         echo "BuildKit ready"
//         break
//     fi
//     sleep 1
//     done
    
//     echo "Buildkidd Log start"
//     echo "==========================="
//     cat /home/jenkins/buildkit/buildkitd.log
//     echo "==========================="

//     # 이미지 빌드 + 푸시
//     buildctl --addr unix://${BUILDKIT_SOCK} build \
//     --frontend dockerfile.v0 \
//     --local context=. \
//     --local dockerfile=. \
//     --output type=image,name=lyh4215/jenkins-study-app:${IMAGE_TAG},push=true
// '''