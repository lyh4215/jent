package org.jent.core.registry

import com.cloudbees.groovy.cps.NonCPS

abstract class BaseRegistryState implements Serializable {

    protected static Object resolveRunKey(def script) {
        Object run = script?.currentBuild?.rawBuild
        return run ?: script
    }

    @NonCPS
    protected static <T> T getOrCreate(Map<Object, T> registries, Object key, Closure<T> creator) {
        synchronized (registries) {
            T existing = registries.get(key)
            if (existing != null) {
                return existing
            }

            T created = creator.call()
            registries.put(key, created)
            return created
        }
    }
}
