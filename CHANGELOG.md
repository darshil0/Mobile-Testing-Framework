# Changelog

All notable changes to this project will be documented in this file.

## [1.5.1] - 2026-05-04

### Added
- Added `listeners.TestListener` to `src/main/java/listeners/` and configured it in `testng.xml` for automated logging and screenshot capture on failure.
- Implemented `scrollToElement` in `utils.TestUtils` by leveraging `utils.GestureHelper`.
- Added missing `By` locator support for `waitForElementToBeClickable` in `utils.WaitHelper`.

### Changed
- Improved `utils.ConfigReader` with robust null checks and regex-based environment variable resolution (supports multiple placeholders in a single string).
- Refined `com.example.BaseTest` to default to the "android" platform if none is specified.
- Refactored `com.example.ExampleTest` to use centralized `utils.WaitHelper` for element waits, improving consistency.
- Standardized project structure by moving `TestListener` to the `main` source set as per documentation.
- Applied Google Java Format across the entire codebase.

### Fixed
- Fixed potential `NullPointerException` in `ConfigReader` when accessing missing configuration keys.
- Fixed logic in `ConfigReader` that previously only allowed full-string environment variable placeholders.
