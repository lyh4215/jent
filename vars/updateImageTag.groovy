def call() {
    // Apply failure guard internally for deployment operations
    def failurePolicy = new org.company.policy.FailurePolicy(this)
    def guard = new org.company.policy.FailureGuard(failurePolicy)
    
    guard.around(org.company.policy.InjectionPoint.DEPLOY, 'ArgoCD manifest update failed') {
        updateFileAndPush(
            credentialsId: 'classic-pat',
            file: 'my-chart/values.yaml',
            updateCommand: "sed -i 's/tag: .*/tag: ${env.IMAGE_TAG}/g' my-chart/values.yaml",
            commitMessage: "🚀 Update image tag to build-${env.BUILD_NUMBER} [skip ci]",
            branch: 'main'
        )
    }
}
