def call() {
  def stderr = ""
  def exitCode = 0

  try {
    sh 'docker build -t app .'
  } catch (err) {
    stderr = err.getMessage()
    exitCode = 1
  }

  def failType = org.company.ci.FailureClassifier.classify(exitCode, stderr)
  def retries = org.company.ci.RetryPolicy.retryCount(failType)

  retry(retries) {
    if (exitCode != 0) {
      error("Build failed: ${failType}")
    }
  }
}
