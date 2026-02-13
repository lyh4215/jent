package org.company.core.failure

interface FailureAction extends Serializable {
    void execute(def script, FailureContext ctx)
}
