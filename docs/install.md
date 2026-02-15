# Install

Jent is a Jenkins Shared Library for Scripted Pipelines.

## Jenkins Setup

1. Go to Jenkins global configuration for Shared Libraries.
2. Add a new library entry:
   - Name: `jent` (or your preferred alias)
   - Default version: `main` (or a tag like `v0.1.0`)
   - Retrieval: your Git repository for this project
3. Save configuration.

## Jenkinsfile Usage

```groovy
@Library('jent@main') _
```

For stable usage in production, prefer a release tag:

```groovy
@Library('jent@v0.1.0') _
```
