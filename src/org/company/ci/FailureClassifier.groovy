package org.company.ci

class FailureClassifier {

  static String classify(int exitCode, String stderr) {
    if (exitCode == 124) {
      return "TIMEOUT"
    }
    if (stderr.contains("network") || stderr.contains("timeout")) {
      return "INFRA"
    }
    return "CODE"
  }
}
