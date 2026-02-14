package org.jent.core.when

class WhenOperation implements Serializable {

    WhenPolicy of(Object policy) {
        return WhenPolicyResolver.resolve(policy)
    }

    WhenPolicy and(List policies) {
        return new AndPolicy(policies: policies)
    }

    WhenPolicy or(List policies) {
        return new OrPolicy(policies: policies)
    }

    WhenPolicy not(Object policy) {
        return new NotPolicy(policy: policy)
    }
}
