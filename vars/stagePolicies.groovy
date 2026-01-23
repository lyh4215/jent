import org.company.policy.AllOfPolicy
import org.company.policy.CiEnabledPolicy
import org.company.policy.MainBranchPolicy
import org.company.policy.NotPullRequestPolicy
import org.company.policy.PreviewPolicy

def deploy() {
    return new AllOfPolicy([
        new CiEnabledPolicy(),
        new MainBranchPolicy(),
        new NotPullRequestPolicy(),
        new PreviewPolicy()
    ])
}

def build() {
    return new AllOfPolicy([
        new CiEnabledPolicy(),
        new MainBranchPolicy()
    ])
}

def test() {
    return new AllOfPolicy([
        new CiEnabledPolicy()
    ])
}

def previewBuild() {
    return new AllOfPolicy([
        new CiEnabledPolicy(),
        new PreviewPolicy()
    ])
}