package org.company.core.registry

import java.util.Collections
import java.util.WeakHashMap

abstract class BaseRegistryState<T> implements Serializable {

    private final Map<Object, T> registries =
            Collections.synchronizedMap(new WeakHashMap<Object, T>())

    protected abstract T newRegistry()

    protected Map<Object, T> registryMap() {
        return registries
    }

    T getOrCreate(def script) {
        Object key = buildKey(script)
        Map<Object, T> map = registryMap()
        T existing = map.get(key)
        if (existing != null) {
            return existing
        }

        T created = newRegistry()
        map.put(key, created)
        return created
    }

    static Object buildKey(def script) {
        Object run = script?.currentBuild?.rawBuild
        if (run == null) {
            // local/test fallback: isolate by script instance
            run = script
        }
        return run
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
