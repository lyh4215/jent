package org.company.features.stageAllowable

import org.company.policy.StagePolicy

class StageGate implements Serializable {

    /**
     * Run body only if policy allows
     */
    static void stageIfAllowed(
        def script,
        String stageName,
        List<StagePolicy> policies,
        Closure body
    ) {
        StagePolicy policy = new CompositeStagePolicy(policies)

        if (policy.isAllowed()) {
            script.stage(stageName) {
                body.call()
            }
        } else {
            script.echo "⏭️ Stage '${stageName}' skipped by policy"
        }
    }

    static void stageIfAllowed(
        def script,
        String stageName,
        StagePolicy policy,
        Closure body
    ) {
        stageIfAllowed(script, stageName, [policy], body)
    }
}
