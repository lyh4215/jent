package org.company.chaos

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
