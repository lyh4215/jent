import org.company.core.chaos.ChaosPolicy
import org.company.core.chaos.ChaosRegistry
import org.company.core.chaos.ChaosRegistryState

def call(ChaosPolicy policy) {
    echo "[CHAOS] register policy='${policy.class.simpleName}', build='${ChaosRegistryState.currentBuildRef(this)}'"
    ChaosRegistry.register(this, policy)
}
