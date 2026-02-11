# jenkins-shared-library best practice

- Trying to make shared library with best practice

```groovy

Stage("deploy") {

    ///some logic
}

class CustomAction implements FailureAction {
    
    @Override
    void execute(def script, FailureContext ctx) {
        //custom action logic
    }
}

class AllAction implements FailureAction {
    @Override
    void execute(def script, FailureContext ctx) {
        //custom action logic
    }
}
나중에 이렇게 가능:

Retry(3) {
    When(MainBranch) {
        Stage("deploy") { ... }
    }
}


이건 완전히 functional composition 구조다.

3️⃣ MSA 사고와도 맞는다

When → execution filter

Retry → execution wrapper

Stage → execution unit

registry.addFailureHandler("deploy", CustomAction)

registry.addFailureHandler(AllAction)

```

++ StageResult 합성 규칙을 코드로 고정해라.