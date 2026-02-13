package org.company.core.when

class SkipStageException extends RuntimeException implements Serializable {
    SkipStageException(String message) {
        super(message)
    }
}
