package org.company.util

interface CiLogger extends Serializable {
    void logFailure(def script, FailureEvent event)
}
