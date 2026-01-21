def call(Map args = [:]) {
    def pipeline = env.JOB_NAME ?: "unknown"
    def stage    = args.stage ?: "unknown"
    def result   = args.result ?: "UNKNOWN"
    def reason   = args.reason ?: "NONE"

    echo "[CI_STATUS] pipeline=${pipeline} stage=${stage} result=${result} reason=${reason}"
}