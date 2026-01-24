import org.company.features.stageAllowable.*

def deploy() {
    return new CompositeStagePolicy([
        new CiEnabledPolicy(),
        new MainBranchPolicy(),
        new NotPullRequestPolicy(),
        new PreviewPolicy()
    ])
}

def build() {
    return new CompositeStagePolicy([
        new CiEnabledPolicy(),
        new MainBranchPolicy()
    ])
}

def test() {
    return new CompositeStagePolicy([
        new CiEnabledPolicy()
    ])
}

def previewBuild() {
    return new CompositeStagePolicy([
        new CiEnabledPolicy(),
        new PreviewPolicy()
    ])
}