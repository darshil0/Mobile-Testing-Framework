# Mobile Testing Framework

An Appium 2 + TestNG framework for Android and iOS testing. It provides typed driver creation, explicit waits, W3C gestures, screenshots, Allure attachments, retries, deep links, and visual-regression helpers.

## Requirements

- Java 11 or newer
- Maven 3.6 or newer
- Node.js 20+ and Appium 2
- Android SDK/emulator for Android tests
- macOS, Xcode, and an iOS simulator for iOS tests

Install Appium and the platform driver you need:

```bash
npm install --global appium
appium driver install uiautomator2 # Android
appium driver install xcuitest     # iOS
```

## Configuration

Copy the example values in `src/test/resources/config.json` to match the application under test. Every value supports environment variables in the form `${NAME:-default}`. Common overrides are:

- `ANDROID_APP_PATH`, `ANDROID_VERSION`, `ANDROID_DEVICE_NAME`
- `IOS_APP_PATH`, `IOS_VERSION`, `IOS_DEVICE_NAME`, `IOS_BUNDLE_ID`
- `APPIUM_HOST`, `APPIUM_PORT`, and `APPIUM_PATH`

The default suite is Android-only so a normal build does not accidentally require an iOS host. The sample tests use placeholder application locators and must be replaced with locators from your app.

## Running tests

The framework starts and stops a local Appium server automatically.

```bash
# Default Android suite
mvn clean test

# Explicit suites
mvn clean test -DsuiteXmlFile=testng-android.xml
mvn clean test -DsuiteXmlFile=testng-ios.xml

# Override the platform parameter
mvn test -Dplatform=android
mvn test -Dplatform=ios
```

To use an already-running Appium server, set `APPIUM_HOST`, `APPIUM_PORT`, and optionally `APPIUM_PATH`; the framework still starts its local service by default, so disable the lifecycle in your own test base class when managing Appium externally.

## Project layout

```text
src/main/java/com/mobile/testing/
├── exceptions/   DriverException
├── listeners/    TestNG logging, screenshots, retry handling
└── utils/        configuration, drivers, waits, gestures, deep links, visual checks
src/test/java/com/mobile/testing/tests/
├── BaseTest.java
├── SmokeTest.java
├── ExampleTest.java
└── IOSTest.java
```

## Writing tests

Extend `BaseTest`, use accessibility IDs or resource IDs where possible, and prefer explicit waits:

```java
public class LoginTest extends BaseTest {
  @Test
  public void login() {
    WebElement button = WaitHelper.waitForClickability(
        driver, AppiumBy.accessibilityId("login_button"));
    button.click();
  }
}
```

`DriverManager` stores one driver per thread, making TestNG parallel execution safe when each test has isolated device resources. `WaitHelper`, `GestureHelper`, `TestUtils`, and `VisualRegressionHelper` contain the reusable interaction APIs.

## Reports and artifacts

- TestNG reports: `test-output/`
- Allure results: `target/allure-results/`
- Failure screenshots: `reports/screenshots/`
- Visual diffs: `reports/visual-diffs/`

```bash
npm install --global allure-commandline
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

## CI

The Android and iOS workflows under `.github/workflows/` install Appium and upload Allure results and screenshots. They expect the application artifact at the path configured by `ANDROID_APP_PATH` or `IOS_APP_PATH`; update those workflow values for your application before enabling device tests.

## Troubleshooting

- **Connection refused:** verify Appium and the driver with `appium driver list --installed`.
- **No device:** use `adb devices` or `xcrun simctl list devices available`.
- **App not found:** use an absolute `ANDROID_APP_PATH`/`IOS_APP_PATH` and verify the file exists.
- **Session creation fails:** check platform version/device name and run `appium-doctor --android` or `--ios`.
- **Port conflict:** set `APPIUM_PORT` to a free port.

## Contributing

Run `mvn clean test`, format changes with `mvn fmt:format`, and include the device, OS, Appium version, and logs when reporting a failure.

## License

MIT. See [LICENSE](LICENSE).
