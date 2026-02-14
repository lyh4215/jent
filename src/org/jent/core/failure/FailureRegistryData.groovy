package org.jent.core.failure

class FailureRegistryData implements Serializable {
    Map<String, List<FailureAction>> handlers = [:]
    List<FailureAction> globalHandlers = []
}
