import org.company.features.stageAllowable.*

def deploy() {
    return StagePolicy.and(
        new CiEnabledPolicy(),
        new MainBranchPolicy(),
        new NotPullRequestPolicy(),
        new PreviewPolicy()
    )
}

def build() {
    return StagePolicy.and(
        new CiEnabledPolicy(),
        new MainBranchPolicy()
    )
}

def test() {
    return new CiEnabledPolicy()
}

def previewBuild() {
    return StagePolicy.and(
        new CiEnabledPolicy(),
        new PreviewPolicy()
    )
}