import org.jent.core.chaos.ChaosRegistryState
import org.jent.core.logging.VerboseLogger

def call(String id, Closure body) {

    def registry = ChaosRegistryHolder()

    VerboseLogger.log(this, "[CHAOS] invoke id='${id}', build='${ChaosRegistryState.currentBuildRef(this)}'")
    registry.maybeFail(this, id)
    VerboseLogger.log(this, "[CHAOS] no injection for id='${id}', running body")

    body.call()
}
