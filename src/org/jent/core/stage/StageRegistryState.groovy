package org.jent.core.stage

import com.cloudbees.groovy.cps.NonCPS
import java.util.Collections
import java.util.WeakHashMap

class StageRegistryState implements Serializable {

    private static final Map<Object, StageRegistryData> REGISTRIES =
            Collections.synchronizedMap(new WeakHashMap<Object, StageRegistryData>())

    static void registerUnique(def script, String stageId) {
        if (!stageId?.trim()) {
            throw new IllegalArgumentException("Stage id must not be blank")
        }

        Object key = resolveRunKey(script)
        registerUniqueByKey(key, stageId)
    }

    @NonCPS
    private static void registerUniqueByKey(Object key, String stageId) {
        synchronized (REGISTRIES) {
            StageRegistryData data = REGISTRIES.get(key)
            if (data == null) {
                data = new StageRegistryData()
                REGISTRIES.put(key, data)
            }
            if (!data.stageIds.add(stageId)) {
                throw new IllegalArgumentException("Duplicate Stage id detected: '${stageId}'")
            }
        }
    }

    private static Object resolveRunKey(def script) {
        Object run = script?.currentBuild?.rawBuild
        return run ?: script
    }
}
