package org.company.policy

class CiEnabledPolicy implements StagePolicy {
    boolean isAllowed(script) {
        return script.env.SKIP_CI != 'true'
    }
}
