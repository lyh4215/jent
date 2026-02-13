package org.company.chaos

import java.util.Collections
import java.util.WeakHashMap

class ChaosRegistryState implements Serializable {

    private static final Map<Object, ChaosRegistry> REGISTRIES =
            Collections.synchronizedMap(new WeakHashMap<Object, ChaosRegistry>())

    static ChaosRegistry get(def script) {
        Object run = script?.currentBuild?.rawBuild
        if (run == null) {
            // local/test fallback: isolate by script instance
            run = script
        }

        ChaosRegistry existing = REGISTRIES.get(run)
        if (existing != null) {
            return existing
        }

        ChaosRegistry created = new ChaosRegistry()
        REGISTRIES.put(run, created)
        return created
    }

    static String currentBuildRef(def script) {
        try {
            def run = script?.currentBuild?.rawBuild
            if (run == null) {
                return "local-script"
            }
            def fullName = run.parent?.fullName ?: "unknown-job"
            return "${fullName}#${run.number}"
        } catch (ignored) {
            return "unknown-build"
        }
    }
}
