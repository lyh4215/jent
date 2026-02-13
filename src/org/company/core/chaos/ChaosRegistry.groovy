package org.company.core.chaos

class ChaosRegistry implements Serializable {

    static void register(def script, ChaosPolicy policy) {
        def state = ChaosRegistryState.get(script)
        state.policies.add(policy)
        println "[CHAOS] registered policy=${policy.class.simpleName}, total=${state.policies.size()}"
    }

    static void maybeFail(def script, String pointId) {
        def state = ChaosRegistryState.get(script)
        script.echo("[CHAOS] maybeFail point='${pointId}', policyCount=${state.policies.size()}")
        for (ChaosPolicy policy : state.policies) {

            if (policy.matches(pointId, script)) {
                script.echo("[CHAOS] matched policy=${policy.class.simpleName}, injecting failure")
                policy.fail(script, pointId)
            }
        }
    }
}
