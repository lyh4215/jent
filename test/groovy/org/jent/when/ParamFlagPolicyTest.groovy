package org.jent.when

import org.junit.Test

class ParamFlagPolicyTest {

    @Test
    void allowsReturnsFalseWhenParameterIsMissing() {
        def policy = new ParamFlagPolicy()
        def script = new Expando(params: [:])

        assert !policy.allows(script)
    }

    @Test
    void allowsSupportsBooleanExpectedValue() {
        def policy = new ParamFlagPolicy(paramName: 'FLAG', expectedValue: true)

        assert policy.allows(new Expando(params: [FLAG: true]))
        assert policy.allows(new Expando(params: [FLAG: 'true']))
        assert !policy.allows(new Expando(params: [FLAG: 'false']))
    }

    @Test
    void allowsSupportsCaseInsensitiveStringComparison() {
        def policy = new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'Build', ignoreCase: true)
        def script = new Expando(params: [RUN_STAGE: 'build'])

        assert policy.allows(script)
    }

    @Test
    void allowsRespectsCaseWhenIgnoreCaseIsFalse() {
        def policy = new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'Build', ignoreCase: false)
        def script = new Expando(params: [RUN_STAGE: 'build'])

        assert !policy.allows(script)
    }
}
