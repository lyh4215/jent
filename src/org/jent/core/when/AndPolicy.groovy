package org.jent.core.when


class AndPolicy implements WhenPolicy {

    List<Object> policies = []

    @Override
    boolean allows(def script) {
        if (policies == null || policies.isEmpty()) {
            throw new IllegalArgumentException("AndPolicy requires at least one policy")
        }

        for (Object candidate : policies) {
            if (!WhenPolicyResolver.resolve(candidate).allows(script)) {
                return false
            }
        }
        return true
    }
}
