package org.company.core.chaos

interface ChaosPolicy extends Serializable {

    boolean matches(String pointId, def script)

    void fail(def script, String pointId)
}
