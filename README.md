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

Install details: `docs/install.md`

## Quick Start

```groovy
@Library('jent@main') _

import org.jent.when.ParamFlagPolicy
import org.jent.failure.SetBuildDescriptionAction
import org.jent.chaos.ParameterChaosPolicy

OnFailure(new SetBuildDescriptionAction())
RegisterChaos(new ParameterChaosPolicy())

node {
    Stage('Build', [retry: 2, when: new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'build')]) {
        echo 'building...'
    }

    Stage('Deploy') {
        Chaos('deploy.before') {
            echo 'deploying...'
        }
    }
}
```

Before/after examples:
- `docs/examples/with-jent.Jenkinsfile`
- `docs/examples/without-jent.Jenkinsfile`

## Features

- `Stage(id, opts, body)` with `when` and `retry`
- `When()` operations: `of`, `and`, `or`, `not`
- `OnFailure(stageId, action)` and global `OnFailure(action)`
- `RegisterChaos(policy)` and `Chaos(pointId) { ... }`
- Verbose logging via `VERBOSE=true`

API reference: `docs/reference.md`

## Architecture

Jent runtime state is build-scoped and managed through registries:
- `StageRegistryState` -> `StageRegistryData`
- `FailureRegistryState` -> `FailureRegistryData`
- `ChaosRegistryState` -> `ChaosRegistryData`

Architecture notes: `docs/architecture.md`

## Examples

- `docs/examples/README.md`
- `docs/examples/with-jent.Jenkinsfile`
- `docs/examples/without-jent.Jenkinsfile`

## Contributing

- Run tests locally: `gradle test`
- Coverage gate is enforced in CI (JaCoCo)

## Release

- Changelog: `CHANGELOG.md`
- Release process: `docs/release.md`
