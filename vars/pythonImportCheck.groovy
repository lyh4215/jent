// vars/pythonImportCheck.groovy
def call() {
    sh '''#!/usr/bin/env bash
    set -euo pipefail
    pip install -r ./app/requirements.txt --break-system-packages
    python - <<'EOF'
from app.main import app
print("FastAPI app import OK")
EOF
    '''
}
