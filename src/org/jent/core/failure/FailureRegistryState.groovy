package org.jent.core.failure

import org.jent.core.registry.BaseRegistryState
import java.util.Collections
import java.util.WeakHashMap

class FailureRegistryState extends BaseRegistryState {

    private static final Map<Object, FailureRegistryData> REGISTRIES =
            Collections.synchronizedMap(new WeakHashMap<Object, FailureRegistryData>())

    static FailureRegistryData get(def script) {
        Object key = resolveRunKey(script)
        return getOrCreate(REGISTRIES, key, FailureRegistryData)
    }
}
