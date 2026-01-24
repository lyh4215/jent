package org.company.features.stageAllowable

class StageGate implements Serializable {

    /**
     * Run stage body only if condition matches
     */
    static void stageIfAllowed(
        def script,
        String stageName,
        StagePolicy policy,
        Closure body
    ) {
        if (policy.isAllowed(script)) {
            script.stage(stageName) {
                body.call()
            }
        } else {
            script.echo "⏭ Skipping stage '${stageName}': ${policy.reason()}"
        }
    }
}
