# API Reference

## Stage

```groovy
Stage(String id, Map opts = [:], Closure body)
```

Options:
- `retry` (default: `1`)
- `when` (`WhenPolicy` class or instance)

Behavior:
- If `when` does not allow execution, stage is marked `NOT_BUILT`.
- Stage body is retried by `retry`.
- On exception, registered failure actions execute, then exception is re-thrown.

## When

```groovy
When().of(policy)
When().and([policy1, policy2])
When().or([policy1, policy2])
When().not(policy)
```

## OnFailure

```groovy
OnFailure(String stageId, FailureAction action) // stage-specific
OnFailure(FailureAction action)                 // global
```

## Chaos

```groovy
RegisterChaos(ChaosPolicy policy)
Chaos(String pointId) { ... }
```

## Built-in Policies and Actions

When:
- `org.jent.when.BranchPatternPolicy`
- `org.jent.when.ParamFlagPolicy`

Failure:
- `org.jent.failure.FailureLogAction`
- `org.jent.failure.SetBuildDescriptionAction`

Chaos:
- `org.jent.chaos.ParameterChaosPolicy`
- `org.jent.chaos.EnvChaosPolicy`
