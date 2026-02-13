package org.company.core.failure

import java.util.Collections
import java.util.WeakHashMap

class FailureRegistryState implements Serializable {

    private static final Map<Object, FailureRegistryData> REGISTRIES =
            Collections.synchronizedMap(new WeakHashMap<Object, FailureRegistryData>())

    static FailureRegistryData get(def script) {
        Object key = buildKey(script)
        FailureRegistryData existing = REGISTRIES.get(key)
        if (existing != null) {
            return existing
        }

        FailureRegistryData created = new FailureRegistryData()
        REGISTRIES.put(key, created)
        return created
    }

    private static Object buildKey(def script) {
        Object run = script?.currentBuild?.rawBuild
        if (run == null) {
            // local/test fallback: isolate by script instance
            run = script
        }
        return run
    }
}
