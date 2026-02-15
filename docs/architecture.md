# Architecture

Jent keeps runtime state in build-scoped registries keyed by `currentBuild.rawBuild` (with script fallback for local/test contexts).

Registry model:
- `StageRegistryState` -> `StageRegistryData`
- `FailureRegistryState` -> `FailureRegistryData`
- `ChaosRegistryState` -> `ChaosRegistryData`

Design points:
- Weak-keyed registry maps (`WeakHashMap`) for lifecycle-friendly storage.
- `@NonCPS` synchronized get-or-create and mutation sections for CPS-safe behavior.
- Jenkinsfile-facing DSL stays simple (`Stage`, `When`, `OnFailure`, `RegisterChaos`, `Chaos`), while state and policy mechanics remain internal.
