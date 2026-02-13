import org.company.core.chaos.ChaosRegistry
import org.company.core.chaos.ChaosRegistryState

def call(String id, Closure body) {
    echo "[CHAOS] invoke id='${id}', build='${ChaosRegistryState.currentBuildRef(this)}'"
    ChaosRegistry.maybeFail(this, id)
    echo "[CHAOS] no injection for id='${id}', running body"

    body.call()
}
