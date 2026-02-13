import org.company.chaos.ChaosPolicy

def call(ChaosPolicy policy) {

    def registry = ChaosRegistryHolder()
    registry.register(policy)
}
