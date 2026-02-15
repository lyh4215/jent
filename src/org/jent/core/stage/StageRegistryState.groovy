package org.jent.core.stage

import com.cloudbees.groovy.cps.NonCPS
import org.jent.core.registry.BaseRegistryState
import java.util.Collections
import java.util.WeakHashMap

class StageRegistryState extends BaseRegistryState {

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
        StageRegistryData data = getOrCreate(REGISTRIES, key, StageRegistryData)
        synchronized (data) {
            if (!data.stageIds.add(stageId)) {
                throw new IllegalArgumentException("Duplicate Stage id detected: '${stageId}'")
            }
        }
    }
}
