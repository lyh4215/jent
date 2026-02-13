import org.company.core.chaos.ChaosPolicy
import org.company.core.chaos.ChaosRegistryState

def call(ChaosPolicy policy) {

    def registry = ChaosRegistryHolder()
    echo "[CHAOS] register policy='${policy.class.simpleName}', build='${ChaosRegistryState.currentBuildRef(this)}'"
    registry.register(policy)
}
