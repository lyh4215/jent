# Jenkins Shared Library: Higher-Level Pipeline Abstractions

Low-level Jenkins Pipeline script는 금방 복잡해집니다.
이 라이브러리는 반복되는 제어 흐름을 작은 DSL로 끌어올려,
파이프라인 코드를 읽기 쉽고 변경하기 쉬운 형태로 만드는 것을 목표로 합니다.

핵심 아이디어는 간단합니다.
`Stage`, `When`, `Retry`, `Failure`, `Chaos`를 조합 가능한 추상화 레이어로 제공하고,
상태 관리는 내부 Registry로 캡슐화합니다.

## Why

- Jenkinsfile의 if/retry/try-catch 보일러플레이트를 줄입니다.
- 실패 처리와 장애 주입(Chaos)을 명시적으로 선언하게 만듭니다.
- 팀이 공통 패턴을 재사용하도록 강제해 운영 안정성을 올립니다.

## What You Get

- `Stage(id, opts) { ... }`
- `When` 정책 기반 실행 제어 (`opts.when`)
- `Retry` 제어 (`opts.retry`)
- `OnFailure(...)`로 스테이지/전역 실패 핸들러 등록
- `RegisterChaos(...)` + `Chaos(...)`로 장애 주입 포인트 관리
- 내부 Registry 기반 상태 관리 (build-scoped)
- `VERBOSE=true` 환경변수 기반 선택적 상세 로그

## Quick Start

### 1) `Jenkinsfile` (Scripted Pipeline)

```groovy
@Library('jenkins-study-shared-lib@main') _

import org.company.when.MainBranchPolicy
import org.company.failure.FailureLogAction
import org.company.failure.NotifySlack
import org.company.chaos.ParameterChaosPolicy

node {
    properties([
        parameters([
            booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
            string(name: 'CHAOS_POINTS', defaultValue: '')
        ])
    ])

    env.VERBOSE = 'false' // true|1|yes|on 이면 상세 로그 출력

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

옵션:
- `retry` (기본값: `1`)
- `when` (`WhenPolicy` 구현체 클래스 또는 인스턴스)

동작:
- `when` 불일치 시 스테이지를 `NOT_BUILT`로 스킵
- `retry` 횟수만큼 본문 재실행
- 예외 발생 시 등록된 `FailureAction` 실행 후 예외 재전파

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

- `RegisterChaos`로 정책 등록
- `Chaos("point")` 실행 시 등록 정책과 매칭되면 예외 주입
- `ParameterChaosPolicy`는 `CHAOS_ENABLED`, `CHAOS_POINTS` 파라미터를 사용

## Built-in Policies/Actions

When 정책:
- `org.company.when.MainBranchPolicy`
- `org.company.when.NotMainBranchPolicy`

Failure 액션:
- `org.company.failure.FailureLogAction`
- `org.company.failure.NotifySlack`

Chaos 정책:
- `org.company.chaos.ParameterChaosPolicy`
- `org.company.chaos.EnvChaosPolicy`

## Registry Design

이 라이브러리는 내부적으로 Registry를 사용해 상태를 관리합니다.

- `FailureRegistryState` -> `FailureRegistryData`
- `ChaosRegistryState` -> `ChaosRegistryData`/`ChaosRegistry`

설계 포인트:
- 빌드(`currentBuild.rawBuild`) 단위로 상태를 분리
- `WeakHashMap` 기반으로 수명 관리
- 사용자 Jenkinsfile에서 직접 상태 객체를 다루지 않도록 캡슐화

## Verbose Logging

Chaos 관련 디버그 로그는 공통 로거를 통해 제어됩니다.

- 켜기: `env.VERBOSE = 'true'`
- 끄기: 미설정 또는 `false`

허용 값: `true`, `1`, `yes`, `on`

## Philosophy

Jenkinsfile을 “스크립트”가 아니라 “조합 가능한 실행 모델”로 다루는 것이 목표입니다.

- `When` = execution filter
- `Retry` = execution wrapper
- `Stage` = execution unit
- `Failure` = failure side-effect router
- `Chaos` = controlled fault injection

이 프로젝트는 Jenkins 추상화 레벨을 한 단계 올려,
현실적인 운영 파이프라인을 더 안전하고 읽기 쉬운 코드로 바꾸기 위해 만들어졌습니다.
