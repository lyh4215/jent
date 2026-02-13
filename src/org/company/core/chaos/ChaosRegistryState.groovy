package org.company.core.chaos

import org.company.core.registry.BaseRegistryState

class ChaosRegistryState extends BaseRegistryState<ChaosRegistryData> {

    private static final ChaosRegistryState INSTANCE = new ChaosRegistryState()

    static ChaosRegistryData get(def script) {
        return INSTANCE.getOrCreate(script)
    }

    static String currentBuildRef(def script) {
        return BaseRegistryState.currentBuildRef(script)
    }

    @Override
    protected ChaosRegistryData newRegistry() {
        return new ChaosRegistryData()
    }
}
