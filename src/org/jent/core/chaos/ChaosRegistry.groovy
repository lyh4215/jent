package org.jent.core.chaos

import com.cloudbees.groovy.cps.NonCPS
import org.jent.core.logging.VerboseLogger

class ChaosRegistry implements Serializable {

    static void register(def script, ChaosPolicy policy) {
        def data = ChaosRegistryState.get(script)
        int total = addPolicy(data, policy)
        VerboseLogger.log(script, "[CHAOS] registered policy=${policy.class.simpleName}, total=${total}")
    }

    static void maybeFail(def script, String pointId) {
        def data = ChaosRegistryState.get(script)
        List<ChaosPolicy> policies = snapshotPolicies(data)
        VerboseLogger.log(script, "[CHAOS] maybeFail point='${pointId}', policyCount=${policies.size()}")
        for (ChaosPolicy policy : policies) {

            if (policy.matches(pointId, script)) {
                VerboseLogger.log(script, "[CHAOS] matched policy=${policy.class.simpleName}, injecting failure")
                policy.fail(script, pointId)
            }
        }
    }

    @NonCPS
    private static int addPolicy(ChaosRegistryData data, ChaosPolicy policy) {
        synchronized (data) {
            data.policies.add(policy)
            return data.policies.size()
        }
    }

    @NonCPS
    private static List<ChaosPolicy> snapshotPolicies(ChaosRegistryData data) {
        synchronized (data) {
            return new ArrayList<ChaosPolicy>(data.policies)
        }
    }
}
