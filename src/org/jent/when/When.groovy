package org.jent.when

import org.jent.core.when.AndPolicy
import org.jent.core.when.NotPolicy
import org.jent.core.when.OrPolicy
import org.jent.core.when.WhenPolicy
import org.jent.core.when.WhenPolicyResolver

class When implements Serializable {

    static WhenPolicy of(Object policy) {
        return WhenPolicyResolver.resolve(policy)
    }

    static WhenPolicy and(List policies) {
        return new AndPolicy(policies: policies)
    }

    static WhenPolicy or(List policies) {
        return new OrPolicy(policies: policies)
    }

    static WhenPolicy not(Object policy) {
        return new NotPolicy(policy: policy)
    }
}
