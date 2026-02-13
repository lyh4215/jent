package org.company.chaos

class ChaosRegistry implements Serializable {

    private List<ChaosPolicy> policies = []

    void register(ChaosPolicy policy) {
        policies.add(policy)
    }

    void maybeFail(def script, String pointId) {

        for (ChaosPolicy policy : policies) {

            if (policy.matches(pointId, script)) {
                policy.fail(script, pointId)
            }
        }
    }
}
