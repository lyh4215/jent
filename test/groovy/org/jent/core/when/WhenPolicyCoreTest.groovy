package org.jent.core.when

import org.junit.Test

class WhenPolicyCoreTest {

    @Test
    void resolverSupportsClassAndInstance() {
        def fromClass = WhenPolicyResolver.resolve(AlwaysTruePolicy)
        def instance = new AlwaysFalsePolicy()
        def fromInstance = WhenPolicyResolver.resolve(instance)

        assert fromClass instanceof AlwaysTruePolicy
        assert fromInstance.is(instance)
    }

    @Test
    void resolverRejectsUnsupportedType() {
        try {
            WhenPolicyResolver.resolve('invalid')
            assert false: 'should throw IllegalArgumentException'
        } catch (IllegalArgumentException e) {
            assert e.message.contains('When requires')
        }
    }

    @Test
    void andPolicyReturnsTrueOnlyWhenAllPoliciesAllow() {
        def allTrue = new AndPolicy(policies: [new AlwaysTruePolicy(), new AlwaysTruePolicy()])
        def mixed = new AndPolicy(policies: [new AlwaysTruePolicy(), new AlwaysFalsePolicy()])

        assert allTrue.allows(new Expando())
        assert !mixed.allows(new Expando())
    }

    @Test
    void andPolicyRejectsEmptyPolicies() {
        try {
            new AndPolicy(policies: []).allows(new Expando())
            assert false: 'should throw IllegalArgumentException'
        } catch (IllegalArgumentException e) {
            assert e.message.contains('at least one policy')
        }
    }

    @Test
    void orPolicyReturnsTrueWhenAnyPolicyAllows() {
        def anyTrue = new OrPolicy(policies: [new AlwaysFalsePolicy(), new AlwaysTruePolicy()])
        def allFalse = new OrPolicy(policies: [new AlwaysFalsePolicy(), new AlwaysFalsePolicy()])

        assert anyTrue.allows(new Expando())
        assert !allFalse.allows(new Expando())
    }

    @Test
    void orPolicyRejectsEmptyPolicies() {
        try {
            new OrPolicy(policies: []).allows(new Expando())
            assert false: 'should throw IllegalArgumentException'
        } catch (IllegalArgumentException e) {
            assert e.message.contains('at least one policy')
        }
    }

    @Test
    void notPolicyInvertsResult() {
        assert !new NotPolicy(policy: new AlwaysTruePolicy()).allows(new Expando())
        assert new NotPolicy(policy: new AlwaysFalsePolicy()).allows(new Expando())
    }

    @Test
    void notPolicyRejectsNullPolicy() {
        try {
            new NotPolicy(policy: null).allows(new Expando())
            assert false: 'should throw IllegalArgumentException'
        } catch (IllegalArgumentException e) {
            assert e.message.contains('requires a policy')
        }
    }

    static class AlwaysTruePolicy implements WhenPolicy {
        @Override
        boolean allows(def script) {
            return true
        }
    }

    static class AlwaysFalsePolicy implements WhenPolicy {
        @Override
        boolean allows(def script) {
            return false
        }
    }
}
