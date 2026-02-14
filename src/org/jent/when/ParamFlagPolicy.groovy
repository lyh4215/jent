package org.jent.when

import org.jent.core.when.WhenPolicy

class ParamFlagPolicy implements WhenPolicy {

    String paramName = "RUN_STAGE"
    Object expectedValue = true
    boolean ignoreCase = true

    @Override
    boolean allows(def script) {
        Object raw = readParam(script, paramName)
        if (raw == null) {
            return false
        }

        if (expectedValue instanceof Boolean) {
            return toBoolean(raw) == ((Boolean) expectedValue)
        }

        String left = raw.toString().trim()
        String right = expectedValue?.toString()?.trim() ?: ""
        if (ignoreCase) {
            return left.equalsIgnoreCase(right)
        }
        return left == right
    }

    private static Object readParam(def script, String key) {
        try {
            return script?.params?."${key}"
        } catch (ignored) {
            return null
        }
    }

    private static boolean toBoolean(Object raw) {
        if (raw instanceof Boolean) {
            return raw
        }
        return raw.toString().trim().equalsIgnoreCase("true")
    }
}
