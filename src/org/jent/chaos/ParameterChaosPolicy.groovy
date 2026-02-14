package org.jent.chaos

import org.jent.core.chaos.ChaosException
import org.jent.core.chaos.ChaosPolicy
import org.jent.core.logging.VerboseLogger

class ParameterChaosPolicy implements ChaosPolicy {

    @Override
    boolean matches(String pointId, def script) {
        def enabled = isChaosEnabled(script)
        if (!enabled) {
            VerboseLogger.log(script, "[CHAOS] disabled (CHAOS_ENABLED=${safeRead(script, 'CHAOS_ENABLED')})")
            return false
        }

        def rawPoints = safeRead(script, 'CHAOS_POINTS')
        if (!rawPoints) {
            VerboseLogger.log(script, "[CHAOS] enabled but no CHAOS_POINTS configured")
            return false
        }

        def normalizedPointId = normalizeToken(pointId)
        if (!normalizedPointId) {
            VerboseLogger.log(script, "[CHAOS] empty pointId from Chaos() call")
            return false
        }

        def points = normalizePoints(rawPoints.toString())
        def matched = points.contains(normalizedPointId)

        VerboseLogger.log(script, "[CHAOS] check point='${normalizedPointId}', configured=${points}, matched=${matched}")
        return matched
    }

    @Override
    void fail(def script, String pointId) {
        throw new ChaosException("Injected failure at chaos point: ${pointId}")
    }

    private static Object safeRead(def script, String key) {
        try {
            return script?.params?."${key}"
        } catch (ignored) {
            return null
        }
    }

    private static boolean isChaosEnabled(def script) {
        def raw = safeRead(script, 'CHAOS_ENABLED')
        if (raw == null) {
            return false
        }
        if (raw instanceof Boolean) {
            return raw
        }
        return raw.toString().trim().equalsIgnoreCase("true")
    }

    private static List<String> normalizePoints(String rawPoints) {
        return rawPoints
                .split(/[,\s]+/)
                .collect { normalizeToken(it) }
                .findAll { it }
                .unique()
    }

    private static String normalizeToken(Object token) {
        if (token == null) {
            return ""
        }
        return token.toString().trim().toLowerCase()
    }
}
