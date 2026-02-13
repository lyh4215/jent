import org.company.core.chaos.ChaosRegistryState
import org.company.core.logging.VerboseLogger

def call(String id, Closure body) {

    def registry = ChaosRegistryHolder()

    VerboseLogger.log(this, "[CHAOS] invoke id='${id}', build='${ChaosRegistryState.currentBuildRef(this)}'")
    registry.maybeFail(this, id)
    VerboseLogger.log(this, "[CHAOS] no injection for id='${id}', running body")

    body.call()
}
