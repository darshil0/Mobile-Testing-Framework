# Changelog

All notable changes to this project will be documented in this file.

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

### Added / Changed / Fixed
- 🚀 **Final Release:** Consolidates all recent fixes and improvements into a single, stable release.
- 🔧 **Dependency Resolution:** Resolved critical dependency conflicts between Appium (`9.3.0`) and Selenium (`4.19.0`) to ensure a stable build.
- 🔧 **Code Refactoring:** Refactored `BaseTest` to use the centralized `DriverManager`; updated `DriverManager` to use modern Appium 2.x `Options` and capabilities.
- 🐛 **Bug Fixes:** Corrected `BaseTest` package declaration, import statements, and fixed a `NullPointerException` in `ConfigReader`.
- 🔧 **Project Maintenance:** Corrected `.gitignore` filename and removed a redundant directory.
- 📚 **Documentation:** Updated `README.md` with a detailed changelog and troubleshooting steps.
- 🎨 **Formatting:** Applied the Google Java Format to the entire codebase for consistency.

## [1.4.0] - 2025-11-15

### Added / Changed / Fixed
- 🔧 **Dependency Resolution:** Resolved critical dependency conflicts between Appium (`9.3.0`) and Selenium (`4.19.0`) by downgrading Selenium and excluding transitive dependencies.
- 🔧 **Code Refactoring:** Refactored `BaseTest` to use the centralized `DriverManager`; updated `DriverManager` to use modern Appium 2.x `Options` classes.
- 🐛 **Bug Fixes:** Corrected `BaseTest` package declaration, import statements, and fixed a `NullPointerException` in `ConfigReader`.
- 🔧 **Project Maintenance:** Corrected a typo in the `.gitignore` filename; removed a redundant `test` directory from `src/main`.
- 📚 **Documentation:** Updated `README.md` with the latest changes and troubleshooting steps.

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
