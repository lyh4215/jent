def spawn() {
    withCredentials([string(credentialsId: 'JENKINS_AGENT_SECRET', variable: 'JENKINS_AGENT_SECRET')]) {
        // 빌드마다 깨끗하게: 남아있을 수 있는 컨테이너/볼륨 정리 후 up
        sh """
        set -eux
        cd ${AGENT_COMPOSE_DIR}
        docker compose down --volumes --remove-orphans || true
        docker compose up -d
        docker compose ps
        """
    }
}

def cleanup() {
    sh """
        set -eux
        cd ${AGENT_COMPOSE_DIR}
        docker compose down --volumes --remove-orphans || true
    """
}