import org.company.when.SkipStageException
import org.company.failure.FailureRegistry

def call(String id, Closure body) {

    stage(id) {
        try {
            body.call()
        } catch (SkipStageException se) {
            // ✅ 스킵은 실패 훅/실패 처리 대상이 아님
            return
        } catch (Exception e) {
            FailureRegistry.execute(id, this, e)
            throw e
        }
    }
}
