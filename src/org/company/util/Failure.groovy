package org.company.util

class Failure {

    static void fail(
        def script,
        FailureType type,
        String stage,
        String reason,
        boolean retryable = false
    ) {
        script.currentBuild.description = """
type=${type}
stage=${stage}
retryable=${retryable}
reason=${reason}
"""
        script.error(reason)
    }
}
