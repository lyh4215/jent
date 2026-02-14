package org.jent.core.logging

class VerboseLogger implements Serializable {

    static void log(def script, String message) {
        if (!enabled(script)) {
            return
        }
        script.echo(message)
    }

    static boolean enabled(def script) {
        Object raw = readEnv(script, "VERBOSE")
        if (raw == null) {
            // fallback for lowercase env key usage
            raw = readEnv(script, "verbose")
        }
        if (raw == null) {
            return false
        }
        if (raw instanceof Boolean) {
            return raw
        }

        String normalized = raw.toString().trim().toLowerCase()
        return normalized in ["true", "1", "yes", "on"]
    }

    private static Object readEnv(def script, String key) {
        try {
            return script?.env?."${key}"
        } catch (ignored) {
            return null
        }
    }
}
