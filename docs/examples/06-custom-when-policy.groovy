@Library('jent@main') _

import org.jent.core.when.WhenPolicy

// Custom policy: true when latest commit message contains [SKIP CI]
def skipCiPolicy = new WhenPolicy() {
    @Override
    boolean allows(def script) {
        def msg = ''
        try {
            msg = script.sh(returnStdout: true, script: "git log -1 --pretty=%B").trim()
        } catch (ignored) {
            // If git metadata is unavailable, treat as not skipped.
        }
        return msg.contains('[SKIP CI]')
    }
}

node {
    Stage('build') {
        sh 'make build'
    }

    // Run this stage only when commit message does NOT contain [SKIP CI]
    Stage('test', [when: When().not(skipCiPolicy)]) {
        sh 'make test'
    }
}
