package org.jent.core.when


class NotPolicy implements WhenPolicy {

    Object policy

    @Override
    boolean allows(def script) {
        if (policy == null) {
            throw new IllegalArgumentException("NotPolicy requires a policy")
        }
        return !WhenPolicyResolver.resolve(policy).allows(script)
    }
}
