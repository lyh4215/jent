import org.jent.core.chaos.ChaosPolicy
import org.jent.core.chaos.ChaosRegistry
import org.jent.core.chaos.ChaosRegistryState
import org.jent.core.logging.VerboseLogger

def call(ChaosPolicy policy) {

    VerboseLogger.log(this, "[CHAOS] register policy='${policy.class.simpleName}', build='${ChaosRegistryState.currentBuildRef(this)}'")
    ChaosRegistry.register(this, policy)
}
