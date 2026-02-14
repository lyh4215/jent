package org.jent.core.chaos

class ChaosException extends RuntimeException implements Serializable {
    ChaosException(String msg) {
        super(msg)
    }
}
