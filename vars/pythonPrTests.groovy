// vars/pythonPrTests.groovy
def call() {
    echo "🔍 Running PR checks for PR #${env.CHANGE_ID}"
    sh '''#!/usr/bin/env bash
    set -euo pipefail
    pytest \
      --cov=app \
      --cov-report=xml \
      --cov-report=term
    '''
}
