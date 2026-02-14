package org.jent.when

import org.jent.core.when.WhenPolicy

class NotMainBranchPolicy implements WhenPolicy {

    @Override
    boolean allows(def script) {
        return script.env.BRANCH_NAME != "main"
    }
}
