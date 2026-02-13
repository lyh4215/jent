package org.company.chaos

class ParameterChaosPolicy implements ChaosPolicy {

    @Override
    boolean matches(String pointId, def script) {
        def enabled = isChaosEnabled(script)
        if (!enabled) {
            script.echo("[CHAOS] disabled (CHAOS_ENABLED=${safeRead(script, 'CHAOS_ENABLED')})")
            return false
        }

        def rawPoints = safeRead(script, 'CHAOS_POINTS')
        if (!rawPoints) {
            script.echo("[CHAOS] enabled but no CHAOS_POINTS configured")
            return false
        }

        def normalizedPointId = normalizeToken(pointId)
        if (!normalizedPointId) {
            script.echo("[CHAOS] empty pointId from Chaos() call")
            return false
        }

        def points = normalizePoints(rawPoints.toString())
        def matched = points.contains(normalizedPointId)

        script.echo("[CHAOS] check point='${normalizedPointId}', configured=${points}, matched=${matched}")
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
