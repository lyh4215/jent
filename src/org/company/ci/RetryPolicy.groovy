package org.company.ci

class RetryPolicy {

  static int retryCount(String failType) {
    switch (failType) {
      case "INFRA":
        return 2
      case "TIMEOUT":
        return 1
      default:
        return 0
    }
  }
}
