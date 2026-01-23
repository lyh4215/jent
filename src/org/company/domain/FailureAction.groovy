// src/org/company/ci/domain/FailureAction.groovy
package org.company.domain

enum FailureAction {
  FAIL,       // error()
  UNSTABLE,   // unstable
  SKIP        // 그냥 통과


  static InjectionPoint from(String raw) {
    if (!raw) {
      return SKIP
    }

    try {
      return valueOf(raw.trim().toUpperCase())
    } catch (IllegalArgumentException e) {
      return SKIP
    }
  }
}