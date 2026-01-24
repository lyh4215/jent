package org.company.features.stageAllowable

interface StagePolicy extends Serializable {
    boolean isAllowed(script)

    /**
     * 조건이 false일 때 왜 실패했는지 설명
     */
    String reason()

    /* ===== DSL Factory ===== */

    static StagePolicy and(StagePolicy... policies) {
        new AndPolicy(policies.toList())
    }

    static StagePolicy or(StagePolicy... policies) {
        new OrPolicy(policies.toList())
    }

    static StagePolicy not(StagePolicy policy) {
        new NotPolicy(policy)
    }
}
