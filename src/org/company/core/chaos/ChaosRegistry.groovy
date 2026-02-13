package org.company.core.chaos

import org.company.core.logging.VerboseLogger

class ChaosRegistry implements Serializable {

    private final ChaosRegistryData data = new ChaosRegistryData()

    void register(ChaosPolicy policy) {
        register(null, policy)
    }

    void register(def script, ChaosPolicy policy) {
        data.policies.add(policy)
        VerboseLogger.log(script, "[CHAOS] registered policy=${policy.class.simpleName}, total=${data.policies.size()}")
    }

    void maybeFail(def script, String pointId) {
        VerboseLogger.log(script, "[CHAOS] maybeFail point='${pointId}', policyCount=${data.policies.size()}")
        for (ChaosPolicy policy : data.policies) {

            if (policy.matches(pointId, script)) {
                VerboseLogger.log(script, "[CHAOS] matched policy=${policy.class.simpleName}, injecting failure")
                policy.fail(script, pointId)
            }
        }
    }
}
