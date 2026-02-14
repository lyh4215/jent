package org.jent.core.stage

import java.util.Collections
import java.util.LinkedHashSet
import java.util.Set
import java.util.WeakHashMap

class StageRegistryState implements Serializable {

    private static final Map<Object, Set<String>> STAGE_IDS_BY_RUN =
            Collections.synchronizedMap(new WeakHashMap<Object, Set<String>>())

    static void registerUnique(def script, String stageId) {
        if (!stageId?.trim()) {
            throw new IllegalArgumentException("Stage id must not be blank")
        }

        Object key = resolveRunKey(script)
        synchronized (STAGE_IDS_BY_RUN) {
            Set<String> ids = STAGE_IDS_BY_RUN.get(key)
            if (ids == null) {
                ids = new LinkedHashSet<String>()
                STAGE_IDS_BY_RUN.put(key, ids)
            }
            if (!ids.add(stageId)) {
                throw new IllegalArgumentException("Duplicate Stage id detected: '${stageId}'")
            }
        }
    }

    private static Object resolveRunKey(def script) {
        Object run = script?.currentBuild?.rawBuild
        return run ?: script
    }
}
