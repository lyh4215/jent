package org.company.when

class SkipStageException extends RuntimeException implements Serializable {
    SkipStageException(String message) {
        super(message)
    }
}
