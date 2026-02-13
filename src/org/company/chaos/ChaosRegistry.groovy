package org.company.chaos

class ChaosRegistry implements Serializable {

    private List<ChaosPolicy> policies = []

    void register(ChaosPolicy policy) {
        policies.add(policy)
        println "[CHAOS] registered policy=${policy.class.simpleName}, total=${policies.size()}"
    }

    void maybeFail(def script, String pointId) {
        script.echo("[CHAOS] maybeFail point='${pointId}', policyCount=${policies.size()}")
        for (ChaosPolicy policy : policies) {

            if (policy.matches(pointId, script)) {
                script.echo("[CHAOS] matched policy=${policy.class.simpleName}, injecting failure")
                policy.fail(script, pointId)
            }
        }
    }
}
