package org.company.core.chaos

class ChaosRegistry implements Serializable {

    private final ChaosRegistryData data = new ChaosRegistryData()

    void register(ChaosPolicy policy) {
        data.policies.add(policy)
        println "[CHAOS] registered policy=${policy.class.simpleName}, total=${data.policies.size()}"
    }

    void maybeFail(def script, String pointId) {
        script.echo("[CHAOS] maybeFail point='${pointId}', policyCount=${data.policies.size()}")
        for (ChaosPolicy policy : data.policies) {

            if (policy.matches(pointId, script)) {
                script.echo("[CHAOS] matched policy=${policy.class.simpleName}, injecting failure")
                policy.fail(script, pointId)
            }
        }
    }
}
