// vars/pythonArchiveCoverage.groovy
def call() {
    archiveArtifacts artifacts: 'reports/*.xml', allowEmptyArchive: true
    archiveArtifacts artifacts: 'coverage.xml', allowEmptyArchive: true
}
