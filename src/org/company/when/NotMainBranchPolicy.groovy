package org.company.when

class NotMainBranchPolicy implements WhenPolicy {

    @Override
    boolean allows(def script) {
        return script.env.BRANCH_NAME != "main"
    }
}
