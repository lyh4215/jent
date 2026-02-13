package org.company.chaos

import java.util.concurrent.ConcurrentHashMap

class ChaosRegistryState implements Serializable {

    private static final String DEFAULT_KEY = "default"
    private static final Map<String, ChaosRegistry> REGISTRIES = new ConcurrentHashMap<>()

    static synchronized ChaosRegistry get(def script) {
        String key = buildKey(script)

        ChaosRegistry existing = REGISTRIES.get(key)
        if (existing != null) {
            return existing
        }

        // If a registry was created before env vars were populated,
        // keep using that one for this run.
        ChaosRegistry fallback = REGISTRIES.get(DEFAULT_KEY)
        if (fallback != null) {
            REGISTRIES.put(key, fallback)
            return fallback
        }

        ChaosRegistry created = new ChaosRegistry()
        REGISTRIES.put(key, created)
        return created
    }

    static String currentKey(def script) {
        return buildKey(script)
    }

    private static String buildKey(def script) {
        def jobName = safeEnv(script, "JOB_NAME")
        def buildNumber = safeEnv(script, "BUILD_NUMBER")
        if (jobName && buildNumber) {
            return "${jobName}#${buildNumber}"
        }

        // Fallback for non-Jenkins/local execution paths.
        return DEFAULT_KEY
    }

    private static Object safeEnv(def script, String key) {
        try {
            return script?.env?."${key}"
        } catch (ignored) {
            return null
        }
    }
}
