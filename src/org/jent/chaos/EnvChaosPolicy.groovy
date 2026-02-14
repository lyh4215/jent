package org.jent.chaos

import org.jent.core.chaos.ChaosException
import org.jent.core.chaos.ChaosPolicy

class EnvChaosPolicy implements ChaosPolicy {

    @Override
    boolean matches(String pointId, def script) {
        return script.env.FAIL_AT == pointId
    }

    @Override
    void fail(def script, String pointId) {
        throw new ChaosException("Injected failure at chaos point: ${pointId}")
    }
}
