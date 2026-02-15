package org.jent.core.failure

import com.cloudbees.groovy.cps.NonCPS
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
        return getOrCreate(run)
    }

    @NonCPS
    private static FailureRegistryData getOrCreate(Object run) {
        synchronized (REGISTRIES) {
            FailureRegistryData existing = REGISTRIES.get(run)
            if (existing != null) {
                return existing
            }

            FailureRegistryData created = new FailureRegistryData()
            REGISTRIES.put(run, created)
            return created
        }
    }
}
