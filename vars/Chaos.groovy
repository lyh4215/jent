import org.company.chaos.ChaosException

def call(String id, Closure body) {

    def registry = ChaosRegistryHolder()

    // Chaos injection (retry 이전에 판단됨)
    registry.maybeFail(this, id)

    body.call()
}
