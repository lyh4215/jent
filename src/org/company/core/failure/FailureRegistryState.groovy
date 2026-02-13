package org.company.core.failure

import org.company.core.registry.BaseRegistryState

class FailureRegistryState extends BaseRegistryState<FailureRegistryData> {

    private static final FailureRegistryState INSTANCE = new FailureRegistryState()

    static FailureRegistryData get(def script) {
        return INSTANCE.getOrCreate(script)
    }

    @Override
    protected FailureRegistryData newRegistry() {
        return new FailureRegistryData()
    }
}
