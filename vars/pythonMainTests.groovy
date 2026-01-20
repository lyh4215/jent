// vars/pythonMainTests.groovy
def call() {
    echo "🧪 Running full test suite on main"
    sh '''#!/usr/bin/env bash
    set -euo pipefail
    pytest \
      --junitxml=reports/junit.xml \
      --cov=app \
      --cov-report=xml \
      --cov-report=term
    '''
}
