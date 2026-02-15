import org.jent.core.chaos.ChaosRegistry
import org.jent.core.chaos.ChaosRegistryState
import org.jent.core.logging.VerboseLogger

def call(String id, Closure body) {

    VerboseLogger.log(this, "[CHAOS] invoke id='${id}', build='${ChaosRegistryState.currentBuildRef(this)}'")
    ChaosRegistry.maybeFail(this, id)
    VerboseLogger.log(this, "[CHAOS] no injection for id='${id}', running body")

    body.call()
}
