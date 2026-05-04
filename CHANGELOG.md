# Changelog

All notable changes to this project will be documented in this file.

## [1.7.3] - 2026-05-04

### Changed
- **Project Maintenance**: Performed a clean-up of legacy test output artifacts (`test-output`, `reports/screenshots`, `allure-results`) to ensure a pristine workspace state.
- Bumped project version to `1.7.3`.

## [1.7.2] - 2026-05-04

### Added
- **iOS Test Suite**: Created `IOSTest.java` with examples for Accessibility IDs and iOS Predicate Strings.
- **Enhanced CI Binary Resolution**: Added explicit detection of `node` and `appium` paths in GitHub Actions workflows to overcome shell environment inconsistencies.

### Fixed
- **Appium Server Manager Hardening**: Added environment variable support (`NODE_PATH`, `APPIUM_JS_PATH`) and strict startup verification to `AppiumServerManager`.
- **iOS CI Workflow**: Added a dedicated `ios-tests.yml` workflow for automated testing on macOS runners.

### Changed
- Bumped project version to `1.7.2`.

## [1.7.1] - 2026-05-04

### Fixed
- **CI/CD Reliability**: Fixed `pom.xml` `argLine` issue by using `maven-dependency-plugin` to provide a consistent path for the AspectJ weaver.
- **Server Lifecycle**: Moved Appium server management to `BaseTest` (@BeforeSuite/@AfterSuite), making tests completely self-contained and eliminating "Connection Refused" errors in CI.
- **Cross-Platform CI**: Added dedicated `ios-tests.yml` and optimized `android-tests.yml` for GitHub Actions.

### Changed
- Bumped project version to `1.7.1`.
- Simplified CI/CD workflows by removing manual Appium start steps.

## [1.7.0] - 2026-05-04

### Added
- **Allure Reporting**: Integrated Allure TestNG for rich reporting. Screenshots are automatically attached to failed tests.
- **Automatic Retries**: Added `RetryAnalyzer` and `AnnotationTransformer` to automatically retry failed tests based on `testSettings.retryCount` in `config.json`.
- **WebView Support**: Added `switchToWebView`, `switchToNativeContext`, and `getAvailableContexts` to `DriverManager` for hybrid app testing.
- **Appium Server Manager**: Added `AppiumServerManager` for programmatic control of the Appium server lifecycle.
- **Improved WaitHelper**: `customWait` now returns the result of the condition for more fluent assertions.

### Changed
- Bumped project version to `1.7.0`.
- Updated `TestListener` to support Allure attachments.
- Updated `testng.xml` with new listeners for retries and reporting.

## [1.6.0] - 2026-05-04

### Added
- Added `GestureHelper.Direction` enum for type-safe scroll and swipe directions.
- Enhanced `GestureHelper.scrollToElement` to support custom directions (`UP`, `DOWN`, `LEFT`, `RIGHT`).
- Added `ConfigReader.isFullReset()` to retrieve global reset settings from `testSettings`.
- Added support for optional `path` in `appiumServer` configuration within `config.json`.

### Changed
- Refactored `WaitHelper` methods: `customWait` now returns the result of the condition, and `waitForElementToBeInvisible` returns a boolean status.
- Updated `DriverManager` to prioritize `testSettings` for `noReset` and `fullReset` capabilities, ensuring consistency across platforms.
- Exposed `scrollToElement(driver, element, direction)` in `TestUtils`.
- Bumped project version to `1.6.0`.

### Fixed
- Fixed capability resolution in `DriverManager`: `noReset` and `fullReset` now correctly pull from `testSettings` as documented.
- Fixed `ConfigReader.getAppiumUrl()` to correctly handle cases where the Appium server requires a base path (e.g., `/wd/hub`).

## [1.5.2] - 2026-05-04

### Fixed
- Fixed `BaseTest.setUp` using `@Optional("android")` on the `platform` parameter so TestNG no
  longer throws `TestNGException` when the parameter is absent from `testng.xml` or run via
  `mvn test -Dtest=…` without a suite file.
- Fixed `DriverManager.getDriver()` — method now returns `null` instead of throwing
  `IllegalStateException` when the driver has not been initialized, making it safe to call from
  `TestListener.takeScreenshot()`. A new `DriverManager.requireDriver()` method preserves the
  throwing behaviour for callers that expect an active session.
- Fixed `TestListener.takeScreenshot()` screenshot filename to include the `_FAILED_` suffix that
  matches the path documented in the README (`testName_FAILED_yyyyMMdd_HHmmss.png`).
- Fixed `GestureHelper.scrollToElement` — removed the unused `driver` parameter from the private
  `isElementInView` helper; the method was shadowing the outer-scope variable unnecessarily.

### Added
- Added `DriverManager.requireDriver()` — throws `DriverException` (not a raw
  `IllegalStateException`) when no driver session is active, giving callers a typed exception to
  catch.
- Added `GestureHelper.longPress(AppiumDriver, WebElement, int)` overload that accepts seconds as
  an `int`, matching the usage shown in the README (`GestureHelper.longPress(driver, element, 3)`).

### Changed
- Refactored `WaitHelper` to expose return-value methods (`waitForVisibility`,
  `waitForClickability`, `waitForPresence`) that return the located `WebElement`, consistent with
  the README API examples. The old `void` methods (`waitForElementToBeVisible`,
  `waitForElementToBeClickable`, `waitForPresenceOfElement`) are retained as `@Deprecated`
  delegates to avoid breaking existing callsites.
- Updated `ExampleTest` to use the new return-value `WaitHelper` API, eliminating redundant
  `driver.findElement` calls after each wait.
- Bumped `pom.xml` artifact version from `1.0.0` to `1.5.2` to match the actual release history.

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

## [1.5.0] - 2025-11-15

### Added
- Consolidates all recent fixes and improvements into a single stable release.
- Resolved critical dependency conflicts between Appium (9.3.0) and Selenium (4.19.0).
- Refactored `BaseTest` to use centralized `DriverManager`.
- Updated `DriverManager` to use modern Appium 2.x `Options` and capabilities.

### Fixed
- Corrected `BaseTest` package declaration and import statements.
- Fixed `NullPointerException` in `ConfigReader`.
- Corrected `.gitignore` filename and removed redundant directory.

### Changed
- Updated `README.md` with detailed changelog and troubleshooting steps.
- Applied Google Java Format to the entire codebase.

## [1.4.0] - 2025-11-15

### Added
- Resolved critical dependency conflicts between Appium (9.3.0) and Selenium (4.19.0) by downgrading Selenium and excluding transitive dependencies.

### Changed
- Refactored `BaseTest` to use centralized `DriverManager`.
- Updated `DriverManager` to use modern Appium 2.x `Options` classes.
- Updated `README.md` with latest changes and troubleshooting steps.

### Fixed
- Corrected `BaseTest` package declaration and import statements.
- Fixed `NullPointerException` in `ConfigReader`.
- Corrected typo in `.gitignore` filename.
- Removed redundant `test` directory from `src/main`.

## [1.3.0] - 2025-11-15

### Added
- Added `TestUtils` utility class with screenshot and interaction helpers.
- Added `DriverException` for custom exception handling.
- Added `TestListener` with automatic screenshot capture on failures.
- Added comprehensive troubleshooting guide.
- Added CI/CD integration examples (GitHub Actions, Jenkins).
- Added best practices and advanced features documentation.

### Changed
- Fixed TestNG scope issue in `pom.xml` (removed `<scope>test</scope>`).
- Updated dependency versions (Appium 9.3.0, Selenium 4.21.0).
- Added Google Java Format plugin for code consistency.
- Enhanced `BaseTest` with better error handling.
- Restructured documentation with clear sections.

## [1.2.0] - 2025-11-14

### Changed
- Refactored `ConfigReader` to use constants and generic capability getter.
- Refactored `DriverManager` to streamline driver initialization.
- Refactored `WaitHelper` to load default timeout dynamically.
- Introduced `BaseTest` class for driver setup and teardown.
- Updated `ExampleTest` to extend `BaseTest`.
- Updated documentation to reflect latest changes.

## [1.1.0] - 2025-11-13

### Changed
- Refactored `GestureHelper` to use W3C Actions API (replacing deprecated `TouchAction`).
- Enhanced `TestListener` with completed screenshot functionality.
- Centralized driver management in `DriverManager`.
- Added Javadoc comments for utilities and example tests.
- Updated documentation with improvements and best practices.

## [1.0.0] - 2025-11-12

### Added
- Initial framework release.
- Basic Appium + TestNG integration.
- Android and iOS support.
- Configuration management via JSON.
- Basic utility classes.
