# Jent — Pipeline DSL for Jenkins

[![Test](https://github.com/lyh4215/jent/actions/workflows/test.yml/badge.svg)](https://github.com/lyh4215/jent/actions/workflows/test.yml)
[![Release](https://img.shields.io/github/v/release/lyh4215/jent)](https://github.com/lyh4215/jent/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/lyh4215/jent/blob/main/LICENSE)
[![Groovy](https://img.shields.io/badge/language-Groovy-4298B8)](https://groovy-lang.org/)

Jent adds a higher-level, type-oriented abstraction on top of Jenkins Scripted Pipelines so teams can reduce boilerplate and express retry, failure handling, and chaos injection consistently.

## Overview

Problem:
- Jenkinsfiles become noisy and hard to maintain.
- Condition/retry/failure logic gets scattered.
- Teams repeatedly reimplement similar patterns.

Solution:
- Composable primitives: `Stage`, `When`, `Retry`, `OnFailure`, `Chaos`
- Build-scoped internal registry state
- Reusable policy/action abstractions for consistent pipeline behavior

## Install

1. Configure this repository as a Jenkins Shared Library (for example, `jent`) in Jenkins global settings.
2. Load the library in your scripted pipeline:

```groovy
@Library('jent@main') _
```

Install details: [Install Guide](docs/install.md)

## Quick Start

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

### If You Did This Without Jent

<details>
<summary>Show raw scripted pipeline example (without Jent)</summary>

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

Examples:
- [Examples Overview](docs/examples/README.md)

## Features

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

Architecture notes: [Architecture](docs/architecture.md)

## Examples

- [Examples Overview](docs/examples/README.md)

## Contributing

- Run tests locally: `gradle test`
- Coverage gate is enforced in CI (JaCoCo)

## Release

- Changelog: [CHANGELOG](CHANGELOG.md)
- Release process: [Release Process](docs/release.md)
