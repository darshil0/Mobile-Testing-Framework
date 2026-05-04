# Mobile Testing Framework

An Appium + TestNG framework for automating Android and iOS apps. Built around the Page Object Model, with centralized config, typed driver management, and automatic failure screenshots.

---

## What's in the box

| Component | Location | Purpose |
|-----------|----------|---------|
| `DriverManager` | `src/main/java/utils/` | Thread-safe driver init and teardown |
| `BaseTest` | `src/test/java/com/example/` | `@BeforeMethod`/`@AfterMethod` wiring |
| `WaitHelper` | `src/main/java/utils/` | Explicit waits returning `WebElement` |
| `GestureHelper` | `src/main/java/utils/` | W3C Actions: swipe, tap, long-press |
| `TestUtils` | `src/main/java/utils/` | Screenshots, safe clicks, key entry |
| `ConfigReader` | `src/main/java/utils/` | Singleton JSON config with env-var resolution |
| `TestListener` | `src/main/java/listeners/` | Logs test events; captures screenshot on failure and attaches to Allure |
| `RetryAnalyzer` | `src/main/java/listeners/` | Retries failed tests based on config |
| `AppiumServerManager` | `src/main/java/utils/` | Programmatic start/stop of Appium server |
| `DriverException` | `src/main/java/exceptions/` | Typed exception for driver lifecycle errors |

---

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

---

## Setup

### 1. Clone and install

```bash
git clone <repository-url>
cd mobile-testing-framework
mvn clean install
```

### 2. Configure your device

Edit `config/config.json`:

```json
{
  "android": {
    "platformName": "Android",
    "platformVersion": "13.0",
    "deviceName": "Android Emulator",
    "automationName": "UiAutomator2",
    "app": "/path/to/your/app.apk",
    "appPackage": "com.example.app",
    "appActivity": "com.example.app.MainActivity"
  },
  "ios": {
    "platformName": "iOS",
    "platformVersion": "16.0",
    "deviceName": "iPhone 14",
    "automationName": "XCUITest",
    "app": "/path/to/your/app.app",
    "bundleId": "com.example.app"
  },
  "appiumServer": {
    "host": "127.0.0.1",
    "port": "4723",
    "path": "/wd/hub"
  },
  "testSettings": {
    "implicitWait": 15,
    "explicitWait": 30,
    "screenshotOnFailure": true,
    "noReset": true,
    "fullReset": false,
    "retryCount": 2
  }
}
```

Any value can be overridden by an environment variable using `${VAR_NAME:-default}` syntax. For example, setting `ANDROID_VERSION=14.0` in the environment overrides `platformVersion` without touching the file.

### 3. Start Appium

```bash
appium
# or on a custom port
appium --address 127.0.0.1 --port 4723
```

### 4. Run tests

```bash
# All tests (defaults to Android)
mvn clean test

# Explicit platform
mvn clean test -Dplatform=android
mvn clean test -Dplatform=ios

# Single class
mvn test -Dtest=ExampleTest

# Custom suite file
mvn test -DsuiteXmlFile=testng-smoke.xml
```

---

## Project structure

```
mobile-testing-framework/
├── config/
│   └── config.json
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
│   │       └── WaitHelper.java
│   └── test/java/com/example/
│       ├── BaseTest.java
│       └── ExampleTest.java
├── reports/
│   └── screenshots/
├── pom.xml
├── testng.xml
└── README.md
```

---

## Writing tests

Extend `BaseTest`. The `platform` parameter is wired through `testng.xml` and defaults to `android` when absent.

```java
package com.example;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WaitHelper;

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
```

### DriverManager

`getDriver()` returns `null` when no session is active — safe for use in listeners and teardown code. Use `requireDriver()` when the test logic genuinely requires an active session and should fail loudly if one isn't present.

```java
AppiumDriver driver = DriverManager.getDriver();      // null if not initialized
AppiumDriver driver = DriverManager.requireDriver();  // throws DriverException if null

// WebView Context Switching
DriverManager.switchToWebView();                      // switches to first available WebView
DriverManager.switchToNativeContext();                // switches back to NATIVE_APP
Set<String> contexts = DriverManager.getAvailableContexts();
```

---

## Test reporting

TestNG writes HTML reports to `test-output/` after each run:

```bash
open test-output/index.html       # macOS
xdg-open test-output/index.html   # Linux
start test-output/index.html      # Windows
```

mvn surefire-report:report
open target/site/surefire-report.html
```

### Allure Reports

The framework is integrated with Allure. Results are generated in `target/allure-results`.

To generate and open the report:
```bash
# Install allure-commandline first: npm install -g allure-commandline
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

`TestListener` captures a screenshot for every failed test and saves it to:

```
reports/screenshots/testName_FAILED_yyyyMMdd_HHmmss.png
```

---

## CI/CD

### GitHub Actions

The repository includes a production-ready workflow in `.github/workflows/android-tests.yml`. It uses `android-emulator-runner` to provide a real hardware-accelerated environment.

```yaml
name: Android Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '11', distribution: 'temurin' }
      
      - name: Run Tests in Emulator
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 31
          script: |
            appium &
            mvn clean test -Dplatform=android

      - name: Upload Allure Results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: allure-results
          path: target/allure-results
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

---

## Parallel execution

```xml
<suite name="Parallel Suite" parallel="tests" thread-count="2">
    <test name="Android">
        <parameter name="platform" value="android"/>
        <classes><class name="com.example.ExampleTest"/></classes>
    </test>
    <test name="iOS">
        <parameter name="platform" value="ios"/>
        <classes><class name="com.example.ExampleTest"/></classes>
    </test>
</suite>
```

`DriverManager` uses `ThreadLocal` storage, so each thread gets its own driver instance with no contention.

---

## Troubleshooting

**`Connection refused` / session won't start** — Appium isn't running. Start it with `appium`, then confirm it's up: `curl http://127.0.0.1:4723/status`.

**`Device not found`** — Run `adb devices` (Android) or `xcrun xctrace list devices` (iOS). Start your emulator/simulator before launching tests.

**`Port 4723 is already in use`** — Kill the existing process: `lsof -ti:4723 | xargs kill -9`, or start Appium on a different port with `appium --port 4724` and update `config.json` accordingly.

**`App not installed` / `App path not found`** — Use absolute paths in `config.json`. Check file permissions on the `.apk`/`.app`.

**`NoSuchElementException`** — Increase explicit wait timeouts in `config.json`. Use Appium Inspector to verify locators. Check whether the element is in a WebView context rather than the native layer.

**`Could not create new session`** — Run `appium-doctor --android` or `appium-doctor --ios` to diagnose environment issues. Update drivers: `appium driver update uiautomator2`.

For verbose Maven output: `mvn test -X`.

---

## Best practices

**Locators** — prefer accessibility IDs and resource IDs over XPath. When XPath is unavoidable, use relative paths. Store locators as constants in a separate class rather than scattering strings through test methods.

**Waits** — use explicit waits exclusively. `Thread.sleep()` makes tests slow and brittle; `WebDriverWait` with a meaningful condition is always the right choice.

**Test isolation** — each test should set up and clean up its own state. Tests that rely on execution order are fragile.

**Data** — externalise test inputs to JSON or properties files. Use `@DataProvider` for parameterised cases. Keep credentials in environment variables.

**Performance** — set `noReset: true` in `config.json` to skip app reinstallation between tests when state doesn't require a clean install.

---

## Data-driven testing

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

---

## Contributing

1. Fork and create a feature branch: `git checkout -b feature/your-feature`
2. Make changes and run `mvn test` to confirm nothing breaks
3. Format: `mvn fmt:format`
4. Commit with a clear message and open a pull request

For bugs, open a GitHub Issue with steps to reproduce, device/OS details, and relevant logs.

---

## Resources

- [Appium docs](https://appium.io/docs/en/latest/)
- [TestNG docs](https://testng.org/doc/documentation-main.html)
- [Selenium docs](https://www.selenium.dev/documentation/)
- [Appium Discuss](https://discuss.appium.io/)

---

## License

MIT — see [LICENSE](LICENSE) for details.

---

*Made with ❤️ by Darshil*
