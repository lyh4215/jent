import org.company.failure.*

def call(String id, Closure body) {

    stage(id) {
        try {
            body.call()
        } catch (Exception e) {

            // registry 실행
            FailureRegistry.execute(id, this, e)

            // Jenkins에 실패 다시 던지기
            throw e
        }
    }
}
