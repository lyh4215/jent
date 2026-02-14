import org.jent.core.when.AndPolicy
import org.jent.core.when.NotPolicy
import org.jent.core.when.OrPolicy
import org.jent.core.when.WhenPolicyResolver

def call(Object policy) {
    return WhenPolicyResolver.resolve(policy)
}

def and(List policies) {
    return new AndPolicy(policies: policies)
}

def or(List policies) {
    return new OrPolicy(policies: policies)
}

def not(Object policy) {
    return new NotPolicy(policy: policy)
}
