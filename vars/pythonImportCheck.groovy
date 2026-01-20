// vars/pythonImportCheck.groovy
def call() {
    sh '''#!/usr/bin/env bash
    set -euo pipefail
    python - <<'EOF'
from app.main import app
print("FastAPI app import OK")
EOF
    '''
}
