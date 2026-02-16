<h1 align="center">Jent</h1>

[![Test](https://github.com/lyh4215/jent/actions/workflows/test.yml/badge.svg)](https://github.com/lyh4215/jent/actions/workflows/test.yml)
[![Release](https://img.shields.io/github/v/release/lyh4215/jent)](https://github.com/lyh4215/jent/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/lyh4215/jent/blob/main/LICENSE)
[![Groovy](https://img.shields.io/badge/language-Groovy-4298B8)](https://groovy-lang.org/)

Write Jenkins Scripted Pipelines like a framework, not a pile of boilerplate.

If you find Jent useful, please consider giving this repository a star. It helps a lot.

## Overview

### Problem
- Scripted pipelines with dynamic flow often end up with ad-hoc per-stage exception handling.
- Failure behavior is frequently coupled to local stage code instead of a shared, stage-identity model.
- As workflows grow, policy composition (branch + params + runtime signals) becomes hard to standardize.

### Solution
- Stage IDs are registered in a build-scoped registry and treated as stable hook points.
- Failure hooks are attached by stage ID (`OnFailure('id', action)`), not by scattered local `catch` blocks.
- Failure is modeled as a semantic event (stage identity + context), not just a raw exception.
- `When` is composable with explicit `and` / `or` / `not` operators for complex scripted conditions.

## Install
[Install Guide](docs/install.md)

## Quick Start

```groovy
@Library('jent@main') _

node {
    Stage('build') {
        sh 'make build'
    }
}
```

## With Jent vs Without Jent

<details>
<summary>Show full comparison example</summary>

### With Jent

```groovy
@Library('jent@main') _

import org.jent.when.BranchPatternPolicy
import org.jent.failure.FailureLogAction
import org.jent.failure.SetBuildDescriptionAction
import org.jent.chaos.ParameterChaosPolicy

// Optional Jenkins parameters used by ParameterChaosPolicy
properties([
    parameters([
        booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
        string(name: 'CHAOS_POINTS', defaultValue: '')
    ])
])

// Global failure action: always set build description on stage failure
OnFailure(new SetBuildDescriptionAction())
// Stage-specific failure action: only for 'deploy' stage
OnFailure('deploy', new FailureLogAction())
// Register built-in chaos policy (reads CHAOS_* params)
RegisterChaos(new ParameterChaosPolicy())

node {
    Stage('test') {
        sh 'make test'
    }

    // Retry build stage, inject chaos only around make build command
    Stage('build', [retry: 2]) {
        echo 'prepare build context'
        Chaos('build.command') {
            sh 'make build'
        }
    }

    // Run deploy only on main branch
    Stage('deploy', 
        [when: new BranchPatternPolicy(patterns: ['main'])]
        ) {
        sh 'make deploy'
    }
}
```

### Without Jent (Raw Scripted Pipeline)

```groovy
def chaosEnabled = params.CHAOS_ENABLED?.toString()?.toBoolean()
def chaosPoints = (params.CHAOS_POINTS ?: '')
        .toString()
        .split(/[,\s]+/)
        .findAll { it }
        .collect { it.trim().toLowerCase() }
        .toSet()

node {
    stage('test') {
        try {
            sh 'make test'
        } catch (Exception e) {
            currentBuild.description = "Failed at stage: test (${e.message ?: e.class.simpleName})"
            throw e
        }
    }

    stage('build') {
        try {
            retry(2) {
                echo 'prepare build context'
                if (chaosEnabled && chaosPoints.contains('build.command')) {
                    error('Injected failure at chaos point: build.command')
                }
                sh 'make build'
            }
        } catch (Exception e) {
            currentBuild.description = "Failed at stage: build (${e.message ?: e.class.simpleName})"
            throw e
        }
    }

    stage('deploy') {
        if (env.BRANCH_NAME == 'main') {
            try {
                sh 'make deploy'
            } catch (Exception e) {
                currentBuild.description = "Failed at stage: deploy (${e.message ?: e.class.simpleName})"
                echo '[LOG] failedAt=deploy'
                throw e
            }
        } else {
            catchError(buildResult: 'SUCCESS', stageResult: 'NOT_BUILT') {
                error('Skipped by branch condition (main only)')
            }
        }
    }
}
```

</details>

## What You Get

- ID-based hook architecture for stage lifecycle and failure routing
- Stage-scoped and global failure hooks by identity (`OnFailure('id', ...)`, `OnFailure(...)`)
- Composable scripted conditions (`When().and(...)`, `When().or(...)`, `When().not(...)`)
- Runtime fault-injection hooks bound to explicit execution points (`Chaos('point')`)
- Build-scoped registry state for deterministic behavior across nodes/stages
- A consistent model for large scripted pipelines where declarative-style static structure is not enough

Core APIs:
- `Stage(id, opts, body)` with `when` and `retry`
- `When()` operations: `of`, `and`, `or`, `not`
- `OnFailure(stageId, action)` and global `OnFailure(action)`
- `RegisterChaos(policy)` and `Chaos(pointId) { ... }`
- Verbose logging via `VERBOSE=true`

API reference: [Reference](docs/reference.md)

## Architecture

Jent runtime state is build-scoped and managed through registries:
- `StageRegistryState` -> `StageRegistryData`
- `FailureRegistryState` -> `FailureRegistryData`
- `ChaosRegistryState` -> `ChaosRegistryData`

Jent avoids unmanaged global runtime state by binding registry data to the Jenkins build lifecycle.

Architecture notes: [Architecture](docs/architecture.md)

## Examples

- [Examples Overview](docs/examples/README.md)

## Contributing

Bug reports, feature ideas, and discussions are welcome.
If Jent helps your pipeline design, a star would mean a lot.

## Release

- Changelog: [CHANGELOG](CHANGELOG.md)
- Release process: [Release Process](docs/release.md)
