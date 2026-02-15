package org.jent.core.chaos

import com.cloudbees.groovy.cps.NonCPS
import java.util.Collections
import java.util.WeakHashMap

class ChaosRegistryState implements Serializable {

    private static final Map<Object, ChaosRegistryData> REGISTRIES =
            Collections.synchronizedMap(new WeakHashMap<Object, ChaosRegistryData>())

    static ChaosRegistryData get(def script) {
        Object run = script?.currentBuild?.rawBuild
        if (run == null) {
            // local/test fallback: isolate by script instance
            run = script
        }
        return getOrCreate(run)
    }

    @NonCPS
    private static ChaosRegistryData getOrCreate(Object run) {
        synchronized (REGISTRIES) {
            ChaosRegistryData existing = REGISTRIES.get(run)
            if (existing != null) {
                return existing
            }

            ChaosRegistryData created = new ChaosRegistryData()
            REGISTRIES.put(run, created)
            return created
        }
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
