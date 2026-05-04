# Mobile Testing Framework
An Appium + TestNG framework for automating Android and iOS apps. Built around the Page Object Model, with centralized config, typed driver management, and automatic failure screenshots.

***
## What's in the box
| Component | Location | Purpose |
|-----------|----------|---------|
| `DriverManager` | `src/main/java/com/mobile/testing/utils/` | Thread‑safe driver initialization and contextual management |
| `BaseTest` | `src/test/java/com/example/` | `@BeforeMethod`/`@AfterMethod` wiring |
| `WaitHelper` | `src/main/java/com/mobile/testing/utils/` | Explicit waits for elements (visibility, clickability, etc.) |
| `GestureHelper` | `src/main/java/com/mobile/testing/utils/` | W3C Actions: swipe, tap, long‑press |
| `TestUtils` | `src/main/java/com/mobile/testing/utils/` | Screenshots, safe clicks, key entry |
| `ConfigReader` | `src/main/java/com/mobile/testing/utils/` | Singleton JSON config with env‑var resolution |
| `TestListener` | `src/main/java/com/mobile/testing/listeners/` | Logs test events; captures screenshot on failure and attaches to Allure |
| `RetryAnalyzer` | `src/main/java/com/mobile/testing/listeners/` | Retries failed tests based on config |
| `AppiumServerManager` | `src/main/java/com/mobile/testing/utils/` | Programmatic start/stop of Appium server |
| `DeepLinkHelper` | `src/main/java/com/mobile/testing/utils/` | Opens deep link URLs on Android and iOS |
| `VisualRegressionHelper`| `src/main/java/com/mobile/testing/utils/` | Baseline pixel‑match image comparison |
| `DriverException` | `src/main/java/com/mobile/testing/exceptions/` | Typed exception for driver lifecycle errors |

***
## Prerequisites
- Java 11+
- Maven 3.6+
- Node.js (required by Appium)
- Appium 2.x — `npm install -g appium`
- Appium UiAutomator2 driver — `appium driver install uiautomator2`
- Appium XCUITest driver (iOS only) — `appium driver install xcuitest`  
- Android Studio / SDK (Android)
- Xcode (iOS, macOS only)

Verify your environment:

```bash
java -version
mvn -version
appium -v
appium-doctor --android   # or --ios
```

***
## Setup
### 1. Clone and install
```bash
git clone <repository-url>
cd mobile-testing-framework
mvn clean install
```
### 2. Configure your device
Configuration is managed via a JSON file located at `src/test/resources/config.json`.
The framework supports environment variable interpolation out‑of‑the‑box (`${VAR_NAME:-default}`) syntax.  
For example, setting `ANDROID_VERSION=14.0` in the environment overrides `platformVersion` without touching the file.
### 3. Run tests
You do **not** need to manually start Appium; `AppiumServerManager` (integrated into `BaseTest` as of `v1.7.0+`) handles server lifecycle automatically. [linkedin](https://www.linkedin.com/pulse/testng-mobile-automation-best-practices-innovations-2025-dave-balroop-bp82c)

Run tests:

```bash
# All tests (defaults to Android)
mvn clean test

# Explicit platform
mvn clean test -Dplatform=android
mvn clean test -Dplatform=ios

# Single class
mvn test -Dtest=ExampleTest

# Custom suite file (Recommended for CI)
mvn test -DsuiteXmlFile=testng-android.xml
mvn test -DsuiteXmlFile=testng-ios.xml
```

If you ever want to run Appium manually (e.g., for debugging), you can still do:

```bash
appium
# or on a custom port
appium --address 127.0.0.1 --port 4723
```

***
## Project structure
```
mobile-testing-framework/
├── src/
│   ├── main/java/
│   │   ├── exceptions/
│   │   │   └── DriverException.java
│   │   ├── listeners/
│   │   │   └── TestListener.java
│   │   └── utils/
│   │       ├── ConfigReader.java
│   │       ├── DriverManager.java
│   │       ├── GestureHelper.java
│   │       ├── TestUtils.java
│   │       ├── WaitHelper.java
│   ├── test/java/com/example/
│   │   ├── BaseTest.java
│   │   └── ExampleTest.java
├── reports/
│   └── screenshots/
├── pom.xml
├── testng.xml
└── README.md
```

***
## Writing tests
Extend `BaseTest`. The `platform` parameter is wired through `testng.xml` and defaults to `"android"` when absent.

```java
package com.mobile.testing.tests;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.mobile.testing.utils.WaitHelper;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login")
    public void testValidLogin() {
        driver.findElement(AppiumBy.accessibilityId("username")).sendKeys("testuser");
        driver.findElement(AppiumBy.accessibilityId("password")).sendKeys("password123");
        driver.findElement(AppiumBy.accessibilityId("loginButton")).click();

        WebElement welcome = WaitHelper.waitForVisibility(
            driver,
            AppiumBy.xpath("//android.widget.TextView[@text='Welcome']"),
            15
        );
        Assert.assertTrue(welcome.isDisplayed(), "Login failed");
    }
}
```
### WaitHelper
All wait methods return the located `WebElement` — no second `findElement` call needed.

```java
// Visibility
WebElement el = WaitHelper.waitForVisibility(driver, locator, 10);
WebElement el = WaitHelper.waitForVisibility(driver, locator);       // uses config default

// Clickability
WebElement btn = WaitHelper.waitForClickability(driver, locator, 10);

// DOM presence (not necessarily visible)
WebElement item = WaitHelper.waitForPresence(driver, locator, 10);

// Disappearance — returns boolean
boolean gone = WaitHelper.waitForElementToDisappear(driver, locator, 10);
boolean invisible = WaitHelper.waitForElementToBeInvisible(driver, element, 10);

// Custom condition — returns the result (requires 'org.openqa.selenium.support.ui.ExpectedConditions')
WebElement el = WaitHelper.customWait(driver, 10, ExpectedConditions.elementToBeClickable(locator));
```
### GestureHelper
```java
// Directional swipes
GestureHelper.swipeUp(driver);
GestureHelper.swipeDown(driver);
GestureHelper.swipeLeft(driver);
GestureHelper.swipeRight(driver);

// Custom swipe between two points
GestureHelper.swipe(driver, startPoint, endPoint, Duration.ofMillis(800));

// Tap
GestureHelper.tap(driver, element);

// Long press — accepts int seconds or Duration
GestureHelper.longPress(driver, element, 3);
GestureHelper.longPress(driver, element, Duration.ofSeconds(3));

// Scroll until element is visible (defaults to UP)
GestureHelper.scrollToElement(driver, element);
GestureHelper.scrollToElement(driver, element, GestureHelper.Direction.DOWN);
```
### TestUtils
```java
TestUtils.takeScreenshot(driver, "checkout_page");
TestUtils.clickElement(driver, element);      // waits for clickability first
TestUtils.sendKeys(element, "some text");     // clears before typing
TestUtils.waitForElement(driver, element, 10);
TestUtils.scrollToElement(driver, element);   // defaults to UP
TestUtils.scrollToElement(driver, element, GestureHelper.Direction.DOWN);

// Deep linking
TestUtils.openDeepLink(driver, "myapp://profile");
```
### Visual Regression
```java
// Compares current screen with "home_screen.png" in src/test/resources/baselines/
// Auto-saves baseline if missing. Allows 1.5% pixel difference tolerance.
boolean matches = VisualRegressionHelper.verifyScreen(
    driver,
    "home_screen",
    1.5
);
Assert.assertTrue(matches, "Visual mismatch detected");
```
### DriverManager
`getDriver()` returns `null` when no session is active — safe for use in listeners and teardown code.  
Use `requireDriver()` when the test logic genuinely requires an active session and should fail loudly if one isn't present.

```java
AppiumDriver driver = DriverManager.getDriver();      // null if not initialized
AppiumDriver driver = DriverManager.requireDriver();  // throws DriverException if null

// WebView context switching
DriverManager.switchToWebView();                      // switches to first available WebView
DriverManager.switchToNativeContext();                // switches back to NATIVE_APP
Set<String> contexts = DriverManager.getAvailableContexts();
```

***
## Test reporting
TestNG writes HTML reports to `test-output/` after each run:

```bash
open test-output/index.html       # macOS
xdg-open test-output/index.html   # Linux
start test-output/index.html      # Windows
```
### Surefire‑style HTML report
You can also generate a Maven‑style Surefire report:

```bash
mvn surefire-report:report
open target/site/surefire-report.html
```
### Allure Reports
The framework is integrated with Allure. Results are generated in `target/allure-results`.  
First install Allure CLI (often via npm):

```bash
# Install allure-commandline first
npm install -g allure-commandline
```

Generate and open the report:

```bash
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

`TestListener` captures a screenshot for every failed test and saves it to:

```
reports/screenshots/testName_FAILED_yyyyMMdd_HHmmss.png
```

***
## CI/CD
### GitHub Actions (Android)
`.github/workflows/android-tests.yml` runs hardware‑accelerated Android emulator tests using `reactivecircus/android-emulator-runner`. [github](https://github.com/marketplace/actions/android-emulator-runner)

> **Important:** The Android workflow must use `runs-on: macos-latest`. The `reactivecircus/android-emulator-runner` action requires a macOS host to launch x86_64 hardware-accelerated emulators. Ubuntu runners do not support this and will fail at emulator boot.

Appium server lifecycle is handled by `AppiumServerManager` (no manual `appium` command needed in the workflow). `NODE_PATH` and `APPIUM_JS_PATH` are exported after install so `AppiumServerManager` can locate the binaries reliably inside the emulator-runner shell context.

```yaml
name: Android Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: macos-latest   # Required: emulator-runner needs macOS for hardware-accelerated x86_64
    timeout-minutes: 45
    env:
      ANDROID_VERSION: "12.0"
      ANDROID_APP_PATH: ${{ github.workspace }}/app/app-debug.apk  # Update to your APK path
    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Set up JDK 11
        uses: actions/setup-java@v4
        with:
          java-version: '11'
          distribution: 'temurin'
          cache: 'maven'

      - name: Set up Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '20'

      - name: Install Appium & UiAutomator2 driver
        run: |
          npm install -g appium
          appium driver install uiautomator2
          echo "NODE_PATH=$(which node)" >> $GITHUB_ENV
          echo "APPIUM_JS_PATH=$(npm root -g)/appium/build/lib/main.js" >> $GITHUB_ENV

      - name: Run Tests in Emulator
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 31
          target: google_apis
          arch: x86_64
          emulator-options: -no-window -gpu swiftshader_indirect -noaudio -no-boot-anim -camera-back none
          disable-animations: true
          script: mvn clean test -Dplatform=android

      - name: Upload Allure Results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: allure-results
          path: target/allure-results/

      - name: Upload Screenshots
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: test-screenshots
          path: reports/screenshots/
```
### GitHub Actions (iOS)
`.github/workflows/ios-tests.yml` runs tests on macOS runners with Xcode and XCUITest‑based capabilities.

> **Important:** `macOS-latest` ships Xcode 16+ with iOS 18 simulators. Set `IOS_VERSION` and `IOS_DEVICE_NAME` env vars to match what's actually available on the runner — use `xcrun simctl list devices available` (not `xcrun xctrace`, which lists Instruments profiling targets, not simulators) to verify.

```yaml
name: iOS Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: macos-latest
    timeout-minutes: 60
    env:
      IOS_VERSION: "18.0"
      IOS_DEVICE_NAME: "iPhone 16"
      IOS_APP_PATH: ${{ github.workspace }}/app/app-debug.app  # Update to your .app path
    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Set up JDK 11
        uses: actions/setup-java@v4
        with:
          java-version: '11'
          distribution: 'temurin'
          cache: 'maven'

      - name: Set up Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '20'

      - name: Install Appium & XCUITest driver
        run: |
          npm install -g appium
          appium driver install xcuitest
          echo "NODE_PATH=$(which node)" >> $GITHUB_ENV
          echo "APPIUM_JS_PATH=$(npm root -g)/appium/build/lib/main.js" >> $GITHUB_ENV

      - name: List Available iOS Simulators
        run: xcrun simctl list devices available  # xcrun xctrace lists Instruments targets, not simulators

      - name: Run iOS Tests
        run: mvn clean test -Dplatform=ios

      - name: Upload Allure Results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: allure-results-ios
          path: target/allure-results/

      - name: Upload Screenshots
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: test-screenshots-ios
          path: reports/screenshots/
```
### Jenkins
```groovy
pipeline {
    agent any
    stages {
        stage('Setup') {
            steps {
                sh 'mvn clean install -DskipTests'
                sh 'npm install -g appium'
            }
        }
        stage('Test') {
            steps {
                // AppiumServerManager handles server lifecycle programmatically in v1.7.0+
                sh 'mvn test -Dplatform=android'
            }
        }
    }
    post {
        always {
            allure includeProperties: false, results: [[path: 'target/allure-results']]
            archiveArtifacts artifacts: 'reports/screenshots/**/*.png', allowEmptyArchive: true
        }
    }
}
```

***
## Parallel execution
Use TestNG's `parallel="tests"` or `parallel="classes"` with `DriverManager`'s `ThreadLocal` storage so each thread owns its own driver instance with no contention. [linkedin](https://www.linkedin.com/pulse/testng-mobile-automation-best-practices-innovations-2025-dave-balroop-bp82c)

```xml
<suite name="Parallel Suite" parallel="tests" thread-count="2">
    <test name="Android">
        <parameter name="platform" value="android"/>
        lasses>lass name="com.example.ExampleTest"/></classes>
    </test>
    <test name="iOS">
        <parameter name="platform" value="ios"/>
        lasses>lass name="com.example.ExampleTest"/></classes>
    </test>
</suite>
```

***
## Troubleshooting
- **`Connection refused` / session won't start** — Appium isn't running.  
  Start it with `appium`, then confirm: `curl http://127.0.0.1:4723/status`.

- **`Device not found`** — Run `adb devices` (Android) or `xcrun simctl list devices available` (iOS). Start your emulator/simulator before tests.

- **`Port 4723 is already in use`** — Kill the existing process: `lsof -ti:4723 | xargs kill -9`, or start Appium on a different port and update `config.json` accordingly.

- **`App not installed` / `App path not found`** — Use absolute paths in `config.json`. In CI, set the `ANDROID_APP_PATH` or `IOS_APP_PATH` env var in your workflow `env:` block. Check file permissions on the `.apk`/`.app`.

- **`NoSuchElementException`** — Increase explicit wait timeouts in `config.json`. Use Appium Inspector to verify locators and check if the element is in a WebView context.

- **`Could not create new session`** — Run `appium-doctor --android` or `--ios`. Update drivers: `appium driver update uiautomator2`.

- **Android emulator fails to boot in CI** — Ensure `runs-on: macos-latest` in `android-tests.yml`. Ubuntu runners do not support hardware-accelerated x86_64 emulators with `reactivecircus/android-emulator-runner`.

- **iOS session creation fails with `No device found` in CI** — `macOS-latest` runners ship Xcode 16+ with iOS 18 simulators, not iOS 16. Set `IOS_VERSION` and `IOS_DEVICE_NAME` env vars in your workflow to match available simulators. Run `xcrun simctl list devices available` in a workflow step to inspect what's on the runner.

- **`xcrun xctrace list devices` shows no simulators** — Use `xcrun simctl list devices available` instead. `xctrace list devices` lists Instruments profiling targets, not iOS simulators.

- **Appium fails to start in CI with `Cannot find node`** — Export `NODE_PATH` and `APPIUM_JS_PATH` to `$GITHUB_ENV` after installing Appium (see workflow examples). `AppiumServerManager` reads these env vars to locate the binaries when PATH lookup is unreliable inside nested shell contexts.

For verbose logs: `mvn test -X`.

***
## Best practices
- **Locators** — prefer accessibility IDs and resource IDs over XPath. When XPath is unavoidable, use relative paths. Store locators as constants in a separate class.
- **Waits** — use explicit waits exclusively. Avoid `Thread.sleep()`. `WaitHelper`‑style utility methods help keep tests fast and resilient. [testmuai](https://www.testmuai.com/blog/appium-with-testng-tutorial/)
- **Test isolation** — each test should set up and clean up its own state. Avoid order‑dependent tests.
- **Data** — externalize test inputs to JSON or properties files. Use `@DataProvider` and keep credentials in environment variables.
- **Performance** — set `noReset: true` in `config.json` to skip app reinstallation between tests when the state does not require a clean install.

***
## Data‑driven testing
```java
@DataProvider(name = "credentials")
public Object[][] credentials() {
    return new Object[][] {
        {"user1@test.com", "pass1"},
        {"user2@test.com", "pass2"},
    };
}

@Test(dataProvider = "credentials")
public void testLogin(String email, String password) {
    // test body
}
```

***
## Contributing
1. Fork and create a feature branch: `git checkout -b feature/your-feature`.
2. Make changes and run `mvn test` to confirm nothing breaks.
3. Format: `mvn fmt:format`.
4. Commit with a clear message and open a pull request.

For bugs, open a GitHub Issue with:
- Steps to reproduce.
- Device/OS details.
- Relevant logs.

***
## Resources
- [Appium docs](https://appium.io/docs/en/latest/)
- [TestNG docs](https://testng.org/doc/documentation-main.html)
- [Selenium docs](https://www.selenium.dev/documentation/)
- [Appium Discuss](https://discuss.appium.io/)

***
## License
MIT — see [`LICENSE`](LICENSE) for details.

***

*Made with ❤️ by Darshil*
