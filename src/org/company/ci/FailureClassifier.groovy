package org.company.ci

class FailureClassifier {

  static String classify(int exitCode, String stderr) {
    if (exitCode == 124) {
      return "TIMEOUT"
    }

    if (stderr?.toLowerCase()?.contains("network") ||
        stderr?.toLowerCase()?.contains("timeout")) {
      return "INFRA"
    }

    if (exitCode != 0) {
      return "CODE"
    }

    return "SUCCESS"
  }
}
