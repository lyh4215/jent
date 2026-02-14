package org.jent.core.when

interface WhenPolicy extends Serializable {
    boolean allows(def script)
}
