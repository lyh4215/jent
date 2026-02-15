package org.jent.core.logging

import org.junit.Test

class VerboseLoggerReadEnvTest {

    @Test
    void enabledReturnsFalseWhenEnvAccessThrows() {
        def broken = new Object() {
            def getEnv() {
                throw new RuntimeException('env unavailable')
            }
        }

        assert !VerboseLogger.enabled(broken)
    }

    @Test
    void enabledTreatsNumericAndToggleStringsAsTrue() {
        assert VerboseLogger.enabled(new Expando(env: [VERBOSE: '1']))
        assert VerboseLogger.enabled(new Expando(env: [VERBOSE: 'on']))
    }
}
