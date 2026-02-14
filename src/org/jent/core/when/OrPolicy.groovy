package org.jent.core.when


class OrPolicy implements WhenPolicy {

    List<Object> policies = []

    @Override
    boolean allows(def script) {
        if (policies == null || policies.isEmpty()) {
            throw new IllegalArgumentException("OrPolicy requires at least one policy")
        }

        for (Object candidate : policies) {
            if (WhenPolicyResolver.resolve(candidate).allows(script)) {
                return true
            }
        }
        return false
    }
}
