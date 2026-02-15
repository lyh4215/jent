# Examples

Scripted Pipeline examples that compare raw Jenkins logic vs Jent abstractions.

- `with-jent.Jenkinsfile`: same behavior implemented with `Stage`, `When`, `OnFailure`, and `Chaos`.
- `without-jent.Jenkinsfile`: equivalent behavior implemented directly with Jenkins scripted syntax.

Both examples assume:
- `RUN_STAGE` parameter controls which stage runs (`build` or `deploy`)
- `CHAOS_ENABLED` and `CHAOS_POINTS` control chaos injection
- failure flow includes global notification logic (`// notify slack logic`)
