# Release Process

This project uses SemVer and keeps release notes in `CHANGELOG.md`.

## Baseline

- Current baseline version: `0.1.0`
- Tag format: `vMAJOR.MINOR.PATCH` (example: `v0.1.0`)

## Release Checklist

1. Ensure CI is green (`gradle test`).
2. Ensure JaCoCo coverage verification passes.
3. Move notable entries from `[Unreleased]` to a new version section in `CHANGELOG.md`.
4. Commit release metadata updates.
5. Create and push a tag:
   - `git tag vX.Y.Z`
   - `git push origin vX.Y.Z`
6. Create a GitHub Release using the matching changelog section.

## Versioning Rules

- `MAJOR`: incompatible DSL/API changes.
- `MINOR`: backward-compatible feature additions.
- `PATCH`: backward-compatible bug fixes and docs/test-only fixes.

