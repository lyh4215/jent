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


registry.addFailureHandler("deploy", CustomAction)

registry.addFailureHandler(AllAction)

```

++ StageResult 합성 규칙을 코드로 고정해라.