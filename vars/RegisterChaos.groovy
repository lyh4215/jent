import org.company.chaos.ChaosPolicy
import org.company.chaos.ChaosRegistryState

def call(ChaosPolicy policy) {

    def registry = ChaosRegistryHolder()
    echo "[CHAOS] register policy='${policy.class.simpleName}', build='${ChaosRegistryState.currentBuildRef(this)}'"
    registry.register(policy)
}
