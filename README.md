# Jent

Type-oriented abstractions for Jenkins Scripted Pipelines.

Jenkins pipelines are powerful, but raw scripted logic quickly becomes noisy.
Jent adds a higher-level layer on top of Jenkins with composable primitives:
`Stage`, `When`, `Retry`, `Failure`, and `Chaos`.

The goal is simple: make pipeline code easier to read, safer to evolve, and more consistent across teams.

## Why Jent

- Reduce repetitive Jenkinsfile boilerplate (`if`, `retry`, `try/catch`, ad-hoc handlers)
- Express failure handling and fault injection as explicit policies/actions
- Keep build-scoped state inside internal registries instead of leaking state logic into Jenkinsfiles

## What You Get

- `Stage(id, opts) { ... }`
- `When` policy execution control via `opts.when`
- `Retry` control via `opts.retry`
- `OnFailure(...)` for stage-level and global failure handlers
- `RegisterChaos(...)` + `Chaos(...)` for controlled fault injection points
- Build-scoped internal registry state
- Optional verbose logging via `VERBOSE=true`

## Quick Start (Scripted Pipeline)

```groovy
@Library('jenkins-study-shared-lib@main') _

import org.company.when.MainBranchPolicy
import org.company.failure.FailureLogAction
import org.company.failure.NotifySlack
import org.company.chaos.ParameterChaosPolicy

properties([
    parameters([
        booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
        string(name: 'CHAOS_POINTS', defaultValue: '')
    ])
])

node {
    env.VERBOSE = 'false' // true|1|yes|on enables verbose logs

    OnFailure('Deploy', new FailureLogAction())
    OnFailure(new NotifySlack())

    RegisterChaos(new ParameterChaosPolicy())

    Stage('Build', [retry: 2, when: MainBranchPolicy]) {
        echo 'build...'
    }

    Stage('Deploy', [retry: 1]) {
        Chaos('deploy.before') {
            echo 'deploy...'
        }
    }
}
```

## Core APIs

### Stage

```groovy
Stage(String id, Map opts = [:], Closure body)
```

Options:
- `retry` (default: `1`)
- `when` (`WhenPolicy` class or instance)

Behavior:
- If `when` does not allow execution, stage is marked `NOT_BUILT`
- Stage body is retried according to `retry`
- On exception, registered `FailureAction`s are executed, then the exception is re-thrown

### OnFailure

```groovy
OnFailure(String stageId, FailureAction action) // stage-specific
OnFailure(FailureAction action)                 // global
```

### Chaos

```groovy
RegisterChaos(ChaosPolicy policy)
Chaos(String pointId) { ... }
```

- Register one or more chaos policies
- `Chaos("point")` evaluates registered policies and injects failures on match
- `ParameterChaosPolicy` uses `CHAOS_ENABLED` and `CHAOS_POINTS`

## Built-in Policies and Actions

When policies:
- `org.company.when.MainBranchPolicy`
- `org.company.when.NotMainBranchPolicy`

Failure actions:
- `org.company.failure.FailureLogAction`
- `org.company.failure.NotifySlack`

Chaos policies:
- `org.company.chaos.ParameterChaosPolicy`
- `org.company.chaos.EnvChaosPolicy`

## Registry Design

Jent keeps runtime state in internal registries:

- `FailureRegistryState` -> `FailureRegistryData`
- `ChaosRegistryState` -> `ChaosRegistryData` / `ChaosRegistry`

Design points:
- Build-scoped isolation using `currentBuild.rawBuild`
- `WeakHashMap` lifecycle-friendly storage
- Jenkinsfiles do not manage registry internals directly

## Verbose Logging

Chaos-related debug logs are centralized through a shared logger.

- Enable: `env.VERBOSE = 'true'`
- Disable: unset or `false`
- Accepted truthy values: `true`, `1`, `yes`, `on`

## Type-oriented, not fully static-typed

Jent uses interface-driven abstractions (`WhenPolicy`, `FailureAction`, `ChaosPolicy`) and typed domain objects (`FailureContext`, registry data models).

Because Jenkins Scripted Pipeline runs on Groovy with dynamic runtime behavior, this is best described as **type-oriented** or **typed-like abstractions**, not strict compile-time type safety.

## Philosophy

Treat Jenkins pipelines as a composable execution model:

- `When` = execution filter
- `Retry` = execution wrapper
- `Stage` = execution unit
- `Failure` = failure side-effect router
- `Chaos` = controlled fault injection

Jent exists to raise the abstraction level of Jenkins Scripted Pipelines while staying practical for real CI/CD operations.
