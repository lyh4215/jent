import org.company.chaos.ChaosRegistry

def call() {

    if (!binding.hasVariable("_BUILD_CHAOS_REGISTRY")) {
        binding.setVariable("_BUILD_CHAOS_REGISTRY", new ChaosRegistry())
    }

    return binding.getVariable("_BUILD_CHAOS_REGISTRY")
}
