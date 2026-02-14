import org.jent.core.chaos.ChaosPolicy
import org.jent.core.chaos.ChaosRegistryState
import org.jent.core.logging.VerboseLogger

def call(ChaosPolicy policy) {

    def registry = ChaosRegistryHolder()
    VerboseLogger.log(this, "[CHAOS] register policy='${policy.class.simpleName}', build='${ChaosRegistryState.currentBuildRef(this)}'")
    registry.register(this, policy)
}
