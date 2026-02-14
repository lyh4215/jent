package org.jent.core.when

class WhenPolicyResolver implements Serializable {

    static WhenPolicy resolve(Object obj) {
        if (obj instanceof Class) {
            return (WhenPolicy) obj.newInstance()
        }
        if (obj instanceof WhenPolicy) {
            return (WhenPolicy) obj
        }
        throw new IllegalArgumentException("When requires a WhenPolicy class or instance")
    }
}
