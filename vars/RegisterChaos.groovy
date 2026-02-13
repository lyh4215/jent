import org.company.core.chaos.ChaosPolicy
import org.company.core.chaos.ChaosRegistryState
import org.company.core.logging.VerboseLogger

def call(ChaosPolicy policy) {

    def registry = ChaosRegistryHolder()
    VerboseLogger.log(this, "[CHAOS] register policy='${policy.class.simpleName}', build='${ChaosRegistryState.currentBuildRef(this)}'")
    registry.register(this, policy)
}
