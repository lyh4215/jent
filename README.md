<h1 align="center">Jent</h1>

[![Test](https://github.com/lyh4215/jent/actions/workflows/test.yml/badge.svg)](https://github.com/lyh4215/jent/actions/workflows/test.yml)
[![Release](https://img.shields.io/github/v/release/lyh4215/jent)](https://github.com/lyh4215/jent/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/lyh4215/jent/blob/main/LICENSE)
[![Groovy](https://img.shields.io/badge/language-Groovy-4298B8)](https://groovy-lang.org/)

Write Jenkins Scripted Pipelines like a framework, not a pile of boilerplate.

If you find Jent useful, please consider giving this repository a star. It helps a lot.

## Overview

### Problem
- Jenkinsfiles become noisy and hard to maintain.
- Condition/retry/failure logic gets scattered.
- Teams repeatedly reimplement similar patterns.
- Different implementation styles across people make collaboration and review harder.

### Solution
- Composable primitives: `Stage`, `When`, `Retry`, `OnFailure`, `Chaos`
- Build-scoped internal registry state
- Reusable policy/action abstractions for consistent pipeline behavior
- A constrained implementation model that promotes team-wide format consistency, long-term maintainability, and scalable project evolution

## Install

1. Configure this repository as a Jenkins Shared Library (for example, `jent`) in Jenkins global settings.
2. Load the library in your scripted pipeline:

```groovy
@Library('jent@main') _
```

Recommended for production stability:

```groovy
@Library('jent@v0.1.0') _
```

Install details: [Install Guide](docs/install.md)

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

properties([
    parameters([
        booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
        string(name: 'CHAOS_POINTS', defaultValue: '')
    ])
])

OnFailure(new SetBuildDescriptionAction())
OnFailure('deploy', new FailureLogAction())
RegisterChaos(new ParameterChaosPolicy())

node {
    Stage('test') {
        sh 'make test'
    }

    Stage('build', [retry: 2]) {
        echo 'prepare build context'
        Chaos('build.command') {
            sh 'make build'
        }
    }

    Stage('deploy', [when: new BranchPatternPolicy(patterns: ['main'])]) {
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

- Deterministic stage execution policies
- Centralized failure handling
- Composable condition logic
- Built-in chaos testing hooks
- Build-scoped state isolation
- Less Jenkinsfile boilerplate
- Team-wide implementation consistency
- Better project sustainability and extensibility as pipelines grow

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
