package org.jent.when

import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import org.junit.Test

class BranchPatternPolicyTest {

    @Test
    void allowsMatchesDefaultMainPattern() {
        def policy = new BranchPatternPolicy()
        def script = new Expando(env: [BRANCH_NAME: 'main'])

        assert policy.allows(script)
    }

    @Test
    void allowsReturnsFalseWhenBranchIsMissing() {
        def policy = new BranchPatternPolicy(patterns: ['main'])
        def script = new Expando(env: [:])

        assert !policy.allows(script)
    }

    @Test
    void allowsSupportsWildcardPatterns() {
        def policy = new BranchPatternPolicy(patterns: ['feature/*', 'release-?'])

        assert policy.allows(new Expando(env: [BRANCH_NAME: 'feature/login']))
        assert policy.allows(new Expando(env: [BRANCH_NAME: 'release-1']))
        assert !policy.allows(new Expando(env: [BRANCH_NAME: 'release-10']))
    }

    @Test
    void allowsTreatsRegexMetaCharactersAsLiterals() {
        def policy = new BranchPatternPolicy(patterns: ['main.v2'])

        assert policy.allows(new Expando(env: [BRANCH_NAME: 'main.v2']))
        assert !policy.allows(new Expando(env: [BRANCH_NAME: 'mainXv2']))
    }

    @Test
    void allowsSupportsMultiplePatternsProvidedAsList() {
        def policy = new BranchPatternPolicy(patterns: ['main', 'release/*', 'hotfix/*'])

        assert policy.allows(new Expando(env: [BRANCH_NAME: 'release/2026.02']))
        assert policy.allows(new Expando(env: [BRANCH_NAME: 'hotfix/urgent']))
        assert !policy.allows(new Expando(env: [BRANCH_NAME: 'feature/new-ui']))
    }

    @Test
    void constructorRejectsStringPatternsInput() {
        try {
            new BranchPatternPolicy(patterns: 'main, release/*')
            assert false: 'patterns should require List<String>'
        } catch (GroovyCastException ignored) {
            // expected
        }
    }
}
