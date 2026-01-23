package org.company.domain

enum InjectionPoint {
  NONE,
  TEST,
  DOCKER_BUILD,
  PUSH,
  DEPLOY,
  VERIFY

  static List<String> names() {
      def result = []
      for (InjectionPoint p : values()) {
        result.add(p.name())
      }
      return result
    }
}