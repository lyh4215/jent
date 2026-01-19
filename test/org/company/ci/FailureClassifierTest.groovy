package org.company.ci

import org.company.ci.FailureClassifier
import org.company.ci.RetryPolicy

class FailureClassifierTest {

  static void run() {
    assert FailureClassifier.classify(0, "") == "SUCCESS"
    assert FailureClassifier.classify(1, "") == "CODE"
    assert FailureClassifier.classify(124, "") == "TIMEOUT"
    assert FailureClassifier.classify(1, "network error") == "INFRA"

    assert RetryPolicy.retryCount("INFRA") == 2
    assert RetryPolicy.retryCount("TIMEOUT") == 1
    assert RetryPolicy.retryCount("CODE") == 0

    println "✅ All shared-lib policy tests passed"
  }
}
