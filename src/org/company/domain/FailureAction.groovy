// src/org/company/ci/domain/FailureAction.groovy
package org.company.domain

enum FailureAction {
  FAIL,       // error()
  UNSTABLE,   // unstable
  SKIP        // 그냥 통과


  static List<String> names() {
    def result = []
    for (FailureAction p : values()) {
      result.add(p.name())
    }
    return result
  }
}