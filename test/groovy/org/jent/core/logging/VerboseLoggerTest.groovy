package org.jent.core.logging

import org.junit.Test

class VerboseLoggerTest {

    @Test
    void enabledReturnsTrueForUppercaseVerboseBoolean() {
        def script = new Expando(env: [VERBOSE: true])
        assert VerboseLogger.enabled(script)
    }

    @Test
    void enabledSupportsLowercaseVerboseFallback() {
        def script = new Expando(env: [verbose: 'yes'])
        assert VerboseLogger.enabled(script)
    }

    @Test
    void enabledReturnsFalseWhenMissingOrInvalid() {
        assert !VerboseLogger.enabled(new Expando(env: [:]))
        assert !VerboseLogger.enabled(new Expando(env: [VERBOSE: 'nope']))
    }

    @Test
    void logCallsEchoOnlyWhenEnabled() {
        def messages = []
        def enabledScript = new Expando(
                env: [VERBOSE: 'true'],
                echo: { String msg -> messages << msg }
        )
        def disabledScript = new Expando(
                env: [VERBOSE: 'false'],
                echo: { String msg -> messages << msg }
        )

        VerboseLogger.log(enabledScript, 'hello')
        VerboseLogger.log(disabledScript, 'ignored')

        assert messages == ['hello']
    }
}
