package org.company.when

interface WhenPolicy extends Serializable {
    boolean allows(def script)
}
