package org.jent.core.chaos

import org.jent.core.registry.BaseRegistryState
import java.util.Collections
import java.util.WeakHashMap

class ChaosRegistryState extends BaseRegistryState {

    private static final Map<Object, ChaosRegistryData> REGISTRIES =
            Collections.synchronizedMap(new WeakHashMap<Object, ChaosRegistryData>())

    static ChaosRegistryData get(def script) {
        Object key = resolveRunKey(script)
        return getOrCreate(REGISTRIES, key, ChaosRegistryData)
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
