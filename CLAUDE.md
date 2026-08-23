# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is **Perl5 Support for JetBrains IDEs (Camelcade)** - a multi-module IntelliJ Platform plugin providing comprehensive Perl language support. The codebase is primarily Kotlin and Java (~2,884 source files).

## Build Commands

```bash
# Build all plugins
./gradlew buildPlugin

# Build specific plugin (e.g., Template Toolkit)
./gradlew :lang.tt2:buildPlugin

# Run all tests (excludes integration tests by default)
./gradlew test

# Run light tests only (fast, for local development)
./gradlew test -PlightTests=1

# Run heavy tests only (longer running)
./gradlew test -PheavyTests=1

# Run integration tests (requires Perl environment)
./gradlew test -PintegrationTests=1

# Run specific test class
./gradlew test -Pruntest=PerlCompletionResultTest

# Run tests with test data overwrite (updates expected output files)
./gradlew test -Poverwrite

# Build with code coverage
./gradlew build -Pwith_coverage=1

# Launch IDE with plugins for testing
./gradlew runIde

# Launch in split mode (frontend/backend separate)
./gradlew runInSplitMode
```

## Architecture

### Plugin Structure

The project uses a modular multi-plugin architecture:

- `plugin/` - Main Perl5 plugin with sub-modules:
  - `psi/` - Program Structure Interface (AST)
  - `common/`, `backend/`, `frontend/` - Split architecture
  - `debugger/`, `profiler/`, `coverage/` - Development tools
  - `perlbrew/`, `plenv/`, `asdf/`, `berrybrew/` - Version manager integrations
  - `docker/`, `wsl/` - Environment support
  - `cpan/`, `cpanminus/` - Package management
- `tt2/` - Template Toolkit support (common/backend/frontend/split)
- `mojo/` - Mojolicious support (common/backend/frontend/split)
- `mason/` - Mason framework plugins
- `embedded/` - Embedded Perl (`<?` markers) support

### Backend/Frontend/Common Pattern

Each language module follows this structure:
- `*-common/` - Shared language definitions
- `*-backend/` - IDE logic, indexing, analysis (heavy computation)
- `*-frontend/` - UI, syntax highlighting, editing
- `*-frontend/split/` - Remote/WSL development support

### Key Directories

- `grammar/` - JFlex lexer grammar files (`Perl5.skeleton`, `Perl5Templating.skeleton`)
- `buildSrc/` - Custom Gradle plugins and build logic
- `plugin/testFixtures/` - Shared test base classes

## Testing

Prefer functional tests over unit tests unless a unit test is absolutely necessary and requested. Functional tests cover behavior instead of a particular implementation and may cover additional stuff on the way.

Tests are categorized using JUnit `@Category` annotations:

- `@Category(Light.class)` - Fast tests, suitable for local development
- `@Category(Heavy.class)` - Longer running tests, run on CI
- `@Category(Integration.class)` - Full integration tests requiring Perl environment

Test base classes in `plugin/testFixtures/`:
- `PerlLightTestCaseBase` - For light/unit tests
- `PerlPlatformTestCase` - For integration tests
- `PerlInstrumentationTestCase` - For instrumentation tests

## Build Configuration

Key files:
- `gradle.properties` - Version numbers and build properties
- `build.gradle.kts` - Root build configuration
- `settings.gradle.kts` - Multi-project module definitions

Current targets:
- Java 21
- IntelliJ Platform 253 (EAP)
- Perl 5.38.0 (for testing)

## Contribution Guidelines

- One PR = one feature or bugfix
- No partial implementations - complete solutions or decompose into separate issues
- PRs must not break existing tests
- Contact maintainer (hurricup) before major architectural changes

### Commit policy

- Prefer atomic changes, not a single commit mess. Commit incrementally as you work, one logical change per commit (e.g. "add key", "add field type", "handle new case in consumers", "add tests" are each their own commit). Don't bundle a whole feature into one commit. Each commit must still build/compile on its own. Use nice messages (IntelliJ format).
- Reference issues in commit messages with a bare `#xxx` (no `user/project` prefix needed for local issues). Always include the issue id in the subject of every commit that belongs to an issue — including test-only, refactor, cleanup, docs, and follow-up commits. Keep a descriptive label if useful, but prefix it with the id, e.g. "#xxx tests: …". Only genuinely issue-less changes (incidental typo, unrelated drive-by) may omit an id.
- Addressing review comments / fixing an existing change: if the code being fixed lives on a not-yet-merged feature branch, make `git commit --fixup=<sha>` commits targeting the original commit that introduced it (the maintainer runs the autosquash rebase). Only if the original change is already in master, make a normal separate commit. Grouping: one fix per commit by default, but several comments about the same problem or a really related issue may share a commit.
- To reword a non-HEAD commit, create a `git commit --fixup=reword:<sha>` commit (or `--fixup=amend:<sha>` to change content+message too) — do NOT reset/cherry-pick or rewrite history by hand. For the current HEAD commit, plain `git commit --amend` is fine.
- Put the why in the commit message, not in inline code comments.
