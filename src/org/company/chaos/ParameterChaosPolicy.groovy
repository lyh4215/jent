package org.company.chaos

class ParameterChaosPolicy implements ChaosPolicy {

    @Override
    boolean matches(String pointId, def script) {

        if (!script.params.CHAOS_ENABLED) {
            return false
        }

        if (!script.params.CHAOS_POINTS) {
            return false
        }

        def points = script.params.CHAOS_POINTS
                .split(',')
                .collect { it.trim() }

        return points.contains(pointId)
    }

    @Override
    void fail(def script, String pointId) {
        throw new ChaosException("Injected failure at chaos point: ${pointId}")
    }
}
