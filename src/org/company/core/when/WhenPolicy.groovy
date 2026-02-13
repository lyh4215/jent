package org.company.core.when

interface WhenPolicy extends Serializable {
    boolean allows(def script)
}
