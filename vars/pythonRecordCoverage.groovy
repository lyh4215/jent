// vars/pythonRecordCoverage.groovy
def call(boolean strict) {
    recordCoverage(
        tools: [[parser: 'COBERTURA', pattern: 'coverage.xml']],
        id: 'coverage',
        name: 'Python Coverage',
        sourceCodeRetention: 'EVERY_BUILD',
        qualityGates: strict ? [
            [threshold: 70.0, metric: 'LINE', baseline: 'PROJECT'],
            [threshold: 60.0, metric: 'BRANCH', baseline: 'PROJECT']
        ] : []
    )
}
