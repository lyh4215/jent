# Examples

Scenario-based Scripted Pipeline examples for Jent.

Scenarios:
- `01-basic.groovy`: minimal Stage usage
- `02-retry.groovy`: retry option with Stage
- `03-branch-policy.groovy`: deploy only on `main` with BranchPatternPolicy
- `04-chaos.groovy`: chaos injection around a specific command
- `05-failure-handling.groovy`: global + stage-specific failure actions
- `06-custom-when-policy.groovy`: skip stage when commit message contains `[SKIP CI]` (via `When().not(...)`)
- `07-custom-failure-action.groovy`: custom `FailureAction` that sends Slack notifications
- `08-when-composition.groovy`: compose `When().and(...)`, `When().or(...)`, and `When().not(...)`

Note:
- These are Groovy-highlighted examples for readability on GitHub.
- Copy the contents into your Jenkinsfile as needed.
