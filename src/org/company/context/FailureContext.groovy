package org.company.context

/**
 * FailureContext
 *
 * Represents contextual information required to
 * handle, log, retry, and route a CI failure.
 *
 * This is NOT a logger, NOT a Jenkins wrapper.
 * It is a pure domain object.
 */
class FailureContext implements Serializable {

    final FailureType type
    final String stage
    final String reason
    final boolean retryable

    /**
     * Optional metadata for future extensions
     * (e.g. owner, severity, service, commit)
     */
    final Map<String, String> metadata

    FailureContext(
        FailureType type,
        String stage,
        String reason,
        boolean retryable = false,
        Map<String, String> metadata = [:]
    ) {
        this.type = type
        this.stage = stage
        this.reason = reason
        this.retryable = retryable
        this.metadata = metadata.asImmutable()
    }

    /**
     * Short human-readable summary
     * (safe for Jenkins description, Slack, etc.)
     */
    String summary() {
        return "${type} @ ${stage}"
    }

    /**
     * Canonical key-value representation
     * (logfmt-compatible, no transport concern)
     */
    Map<String, String> asFields() {
        def fields = [
            'type'      : type.toString(),
            'stage'     : stage,
            'retryable' : retryable.toString(),
            'reason'    : reason
        ]
        return fields + metadata
    }

    /**
     * Derive a new context with exception info attached
     */
    FailureContext withException(Exception e) {
        return new FailureContext(
            this.type,
            this.stage,
            this.reason,
            this.retryable,
            this.metadata + [
                exception       : e.class.simpleName,
                exceptionMessage: e.message
            ]
        )
    }
}
