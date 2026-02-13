package org.company.core.failure

import java.util.Collections
import java.util.WeakHashMap

class FailureRegistryState implements Serializable {

    private static final Map<Object, FailureRegistryData> REGISTRIES =
            Collections.synchronizedMap(new WeakHashMap<Object, FailureRegistryData>())

    static FailureRegistryData get(def script) {
        Object run = script?.currentBuild?.rawBuild
        if (run == null) {
            run = script
        }

        FailureRegistryData existing = REGISTRIES.get(run)
        if (existing != null) {
            return existing
        }

        FailureRegistryData created = new FailureRegistryData()
        REGISTRIES.put(run, created)
        return created
    }
}
