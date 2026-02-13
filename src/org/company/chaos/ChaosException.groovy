package org.company.chaos

class ChaosException extends RuntimeException implements Serializable {
    ChaosException(String msg) {
        super(msg)
    }
}
