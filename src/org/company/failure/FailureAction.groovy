package org.company.failure

interface FailureAction extends Serializable {
    void execute(def script, FailureContext ctx)
}
