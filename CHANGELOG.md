# Changelog

All notable changes to this project are documented in this file.

The format is based on Keep a Changelog.

## [Unreleased]

### Added
- Global variable reference docs in `vars/*.txt` for `Stage`, `When`, `OnFailure`, `Chaos`, and `RegisterChaos`.

### Changed
- Registry states unified via `BaseRegistryState`.
- Chaos registry aligned with failure-style state/data model.
- Registry `getOrCreate` updated to avoid CPS closure mismatch in `@NonCPS` paths.
- Failure registry mutations synchronized in `@NonCPS` sections for parallel-safety.
- Stage registry restored to `WeakHashMap` + `StageRegistryData` model.

### Removed
- Unused `ChaosRegistryHolder` global var step.

## [0.1.0] - 2026-02-15

### Added
- Core DSL primitives for scripted pipelines:
  - `Stage(id, opts, body)`
  - `When()` operations (`of`, `and`, `or`, `not`)
  - `OnFailure(...)` (stage-specific and global)
  - `RegisterChaos(...)` and `Chaos(pointId) { ... }`
- Built-in policies and actions:
  - `BranchPatternPolicy`, `ParamFlagPolicy`
  - `ParameterChaosPolicy`, `EnvChaosPolicy`
  - `FailureLogAction`, `SetBuildDescriptionAction`
- Build-scoped registry state for stage/failure/chaos.
- Verbose logging support via `VERBOSE` env.
- Unit and vars tests with `jenkins-pipeline-unit`.
- CI workflow with JaCoCo summary and coverage verification gate.
- Scripted pipeline examples in `docs/examples`.
- MIT license.

### Changed
- Repository renamed to `jent`.
- Package namespace migrated from `org.company.*` to `org.jent.*`.

