import org.company.chaos.ChaosRegistryState

def call(String id, Closure body) {

    def registry = ChaosRegistryHolder()

    echo "[CHAOS] invoke id='${id}', key='${ChaosRegistryState.currentKey(this)}'"
    registry.maybeFail(this, id)
    echo "[CHAOS] no injection for id='${id}', running body"

    body.call()
}
