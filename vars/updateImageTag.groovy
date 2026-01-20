def call() {
    updateFileAndPush(
        credentialsId: 'classic-pat',
        file: 'my-chart/values.yaml',
        updateCommand: "sed -i 's/tag: .*/tag: ${env.IMAGE_TAG}/g' my-chart/values.yaml",
        commitMessage: "🚀 Update image tag to build-${env.BUILD_NUMBER} [skip ci]",
        branch: 'main'
    )
}
