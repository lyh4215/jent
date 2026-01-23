package org.company.domain

enum InjectionPoint {
  NONE,
  TEST,
  DOCKER_BUILD,
  PUSH,
  DEPLOY,
  VERIFY

  static InjectionPoint from(String raw) {
    if (!raw) {
      return NONE
    }

    try {
      return valueOf(raw.trim().toUpperCase())
    } catch (IllegalArgumentException e) {
      return NONE
    }
  }
}