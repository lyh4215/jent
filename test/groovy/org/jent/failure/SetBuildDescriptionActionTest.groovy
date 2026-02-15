package org.jent.failure

import org.jent.core.failure.FailureContext
import org.junit.Test

class SetBuildDescriptionActionTest {

    @Test
    void executeSetsBaseDescriptionWhenExceptionIsDisabled() {
        def script = new Expando(currentBuild: new Expando(description: null))
        def action = new SetBuildDescriptionAction(includeException: false)
        def ctx = new FailureContext(stageId: 'test', exception: new RuntimeException('boom'))

        action.execute(script, ctx)

        assert script.currentBuild.description == 'Failed at stage: test'
    }

    @Test
    void executeSetsBaseDescriptionWhenExceptionIsMissing() {
        def script = new Expando(currentBuild: new Expando(description: null))
        def action = new SetBuildDescriptionAction(includeException: true)
        def ctx = new FailureContext(stageId: 'build', exception: null)

        action.execute(script, ctx)

        assert script.currentBuild.description == 'Failed at stage: build'
    }

    @Test
    void executeIncludesNormalizedExceptionMessage() {
        def script = new Expando(currentBuild: new Expando(description: null))
        def action = new SetBuildDescriptionAction(includeException: true, maxMessageLength: 120)
        def ctx = new FailureContext(
                stageId: 'deploy',
                exception: new RuntimeException('line1\nline2\r\nline3')
        )

        action.execute(script, ctx)

        assert script.currentBuild.description == 'Failed at stage: deploy (line1 line2 line3)'
    }

    @Test
    void executeFallsBackToExceptionClassNameWhenMessageIsNull() {
        def script = new Expando(currentBuild: new Expando(description: null))
        def action = new SetBuildDescriptionAction(includeException: true, maxMessageLength: 120)
        def exception = new RuntimeException((String) null)
        def ctx = new FailureContext(stageId: 'verify', exception: exception)

        action.execute(script, ctx)

        assert script.currentBuild.description == 'Failed at stage: verify (RuntimeException)'
    }

    @Test
    void executeTruncatesLongExceptionMessage() {
        def script = new Expando(currentBuild: new Expando(description: null))
        def action = new SetBuildDescriptionAction(includeException: true, maxMessageLength: 10)
        def ctx = new FailureContext(stageId: 'release', exception: new RuntimeException('0123456789ABCDEF'))

        action.execute(script, ctx)

        assert script.currentBuild.description == 'Failed at stage: release (0123456789...)'
    }

    @Test
    void executeDoesNotTruncateWhenMessageLengthEqualsLimit() {
        def script = new Expando(currentBuild: new Expando(description: null))
        def action = new SetBuildDescriptionAction(includeException: true, maxMessageLength: 10)
        def ctx = new FailureContext(stageId: 'release', exception: new RuntimeException('0123456789'))

        action.execute(script, ctx)

        assert script.currentBuild.description == 'Failed at stage: release (0123456789)'
    }
}
