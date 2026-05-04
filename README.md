# Mobile Testing Framework

## Overview
This framework provides a robust and scalable structure for automating mobile application tests using **Appium** and **TestNG**. It supports both Android and iOS platforms and promotes maintainable, reusable test code through the Page Object Model (POM) design pattern.

## Features
- **Cross-Platform Support:** Write tests for both Android and iOS platforms
- **Java-Based:** Leverages the widely-used Java programming language (JDK 11+)
- **Maven:** Handles dependencies and project build lifecycle
- **TestNG Integration:** Organize, execute, and report tests efficiently with parallel execution support
- **Centralized Configuration:** Manage environment-specific parameters via `config/config.json`
- **Driver Management:** `DriverManager` and `BaseTest` handle driver initialization and cleanup
- **Utility Helpers:** 
  - `GestureHelper` - Simplify swipes, taps, and long-press actions using W3C Actions API
  - `WaitHelper` - Manage dynamic waits for elements with configurable timeouts
  - `TestUtils` - Screenshot capture, element interactions, and common operations
- **Base Test Class:** `BaseTest` handles driver setup and teardown, reducing boilerplate code
- **Test Reporting:** 
  - Automatic screenshot capture on test failures
  - TestNG HTML reports
  - Surefire XML reports for CI/CD integration
- **Code Formatting:** Integrated Google Java Format for consistent code style
- **Exception Handling:** Custom `DriverException` for better error management

## Prerequisites

### Required Software
- [Node.js & npm](https://nodejs.org/) - Required for Appium
- [Appium](https://appium.io/) - Install via `npm install -g appium`
- [Appium Doctor](https://github.com/appium/appium-doctor) - Verify setup with `npm install -g appium-doctor`
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) - Version 11 or higher
- [Apache Maven](https://maven.apache.org/download.cgi) - Version 3.6+
- [Android Studio](https://developer.android.com/studio) - For Android testing
- [Xcode](https://developer.apple.com/xcode/) - For iOS testing (macOS only)

### Verify Installation
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Appium installation
appium -v

# Verify environment setup
appium-doctor --android  # For Android
appium-doctor --ios      # For iOS
```

## Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd mobile-testing-framework
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Configure Test Environment

#### Update config/config.json
Edit `config/config.json` with your device and app details:

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
  }
}
```

**Configuration Options:**
- Environment variables can override config values (e.g., `ANDROID_VERSION`)
- Update `appiumServer.url` if using remote Appium server
- Set `noReset: true` to skip app reinstallation between tests

### 4. Start Appium Server
```bash
# Start with default settings
appium

# Or specify custom host and port
appium --address 127.0.0.1 --port 4723
```

### 5. Run Tests

#### Run all tests (default: Android)
```bash
mvn clean test
```

#### Run tests for specific platform
```bash
# Android tests
mvn clean test -Dplatform=android

# iOS tests
mvn clean test -Dplatform=ios
```

#### Run specific test class
```bash
mvn test -Dtest=ExampleTest
```

#### Run with custom TestNG suite
```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
```

## Project Structure

```
mobile-testing-framework/
├── .github/
│   └── workflows/
│       └── test.yml              # CI/CD workflow (optional)
├── config/
│   └── config.json               # Environment configurations
├── src/
│   ├── main/java/
│   │   ├── exceptions/
│   │   │   └── DriverException.java    # Custom exception handling
│   │   ├── listeners/
│   │   │   └── TestListener.java       # TestNG listener for reporting
│   │   └── utils/
│   │       ├── BaseTest.java           # Base test class with setup/teardown
│   │       ├── ConfigReader.java       # Configuration file reader
│   │       ├── DriverManager.java      # Driver initialization manager
│   │       ├── GestureHelper.java      # W3C Actions API wrapper
│   │       ├── TestUtils.java          # Common test utilities
│   │       └── WaitHelper.java         # Explicit wait utilities
│   └── test/java/com/example/
│       └── ExampleTest.java            # Sample test cases
├── screenshots/                         # Auto-generated test screenshots
├── reports/                             # Generated test reports
├── target/                              # Maven build output
├── .gitignore                           # Git ignore rules
├── pom.xml                              # Maven project configuration
├── testng.xml                           # TestNG suite configuration
└── README.md                            # This file
```

## Writing Tests

### Example Test Class
```java
package com.example;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.WaitHelper;

public class LoginTest extends BaseTest {
    
    @Test(description = "Verify successful login")
    public void testValidLogin() {
        WebElement usernameField = driver.findElement(
            AppiumBy.accessibilityId("username")
        );
        WebElement passwordField = driver.findElement(
            AppiumBy.accessibilityId("password")
        );
        WebElement loginButton = driver.findElement(
            AppiumBy.accessibilityId("loginButton")
        );
        
        usernameField.sendKeys("testuser");
        passwordField.sendKeys("password123");
        loginButton.click();
        
        WebElement welcomeText = WaitHelper.waitForVisibility(
            driver, 
            AppiumBy.xpath("//android.widget.TextView[@text='Welcome']"),
            15
        );
        
        Assert.assertTrue(welcomeText.isDisplayed(), "Login failed");
    }
}
```

### Using Utilities

#### WaitHelper
```java
// Wait for element visibility
WebElement element = WaitHelper.waitForVisibility(driver, locator, 10);

// Wait for element to be clickable
WebElement button = WaitHelper.waitForClickability(driver, locator, 10);

// Wait for element presence
WebElement item = WaitHelper.waitForPresence(driver, locator, 10);
```

#### GestureHelper
```java
// Swipe actions
GestureHelper.swipeUp(driver, 0.8, 0.2);
GestureHelper.swipeDown(driver, 0.2, 0.8);
GestureHelper.swipeLeft(driver, 0.8, 0.2);
GestureHelper.swipeRight(driver, 0.2, 0.8);

// Tap and long press
GestureHelper.tap(driver, element);
GestureHelper.longPress(driver, element, 3);
```

#### TestUtils
```java
// Take screenshot
TestUtils.takeScreenshot(driver, "login_page");

// Safe element interactions
TestUtils.clickElement(driver, element);
TestUtils.sendKeys(element, "text");
TestUtils.waitForElement(driver, element, 10);
```

## Test Reporting

### TestNG Reports
After test execution, TestNG generates HTML reports:
```
test-output/
├── index.html           # Main report
├── emailable-report.html
└── testng-results.xml
```

View reports:
```bash
open test-output/index.html  # macOS
xdg-open test-output/index.html  # Linux
start test-output/index.html  # Windows
```

### Surefire Reports
Maven Surefire generates additional reports:
```bash
mvn surefire-report:report
open target/site/surefire-report.html
```

### Screenshots
Failed tests automatically capture screenshots saved to:
```
screenshots/
└── testName_FAILED_yyyyMMdd_HHmmss.png
```

## Troubleshooting

### Common Issues

#### 1. Appium Server Not Running
**Error:** `Connection refused` or `Could not start new session`

**Solution:**
```bash
# Start Appium server
appium

# Check if server is running
curl http://127.0.0.1:4723/status
```

#### 2. Device Not Found
**Error:** `Device not found` or `No devices/emulators found`

**Solution:**
```bash
# Check connected Android devices
adb devices

# Check iOS simulators
xcrun xctrace list devices

# Start Android emulator
emulator -avd <emulator_name>
```

#### 3. Port Already in Use
**Error:** `Port 4723 is already in use`

**Solution:**
```bash
# Find and kill process using port 4723
lsof -ti:4723 | xargs kill -9

# Or start Appium on different port
appium --port 4724
```

#### 4. App Not Installed
**Error:** `App not installed` or `App path not found`

**Solution:**
- Verify app path in `config.json` is correct
- Use absolute paths instead of relative paths
- Check file permissions

#### 5. Element Not Found
**Error:** `NoSuchElementException`

**Solution:**
- Increase implicit/explicit wait times
- Verify element locator strategy
- Use Appium Inspector to identify correct locators
- Check if element is in a different context (webview vs native)

#### 6. Session Creation Failed
**Error:** `Could not create new session`

**Solution:**
```bash
# Run Appium Doctor to check setup
appium-doctor --android
appium-doctor --ios

# Update Appium drivers
appium driver update uiautomator2
appium driver update xcuitest
```

### Debug Mode
Run tests with debug logging:
```bash
mvn test -X  # Maven debug mode
```

## CI/CD Integration

### GitHub Actions
Create `.github/workflows/test.yml`:

```yaml
name: Mobile Test Automation

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  android-tests:
    runs-on: macos-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'adopt'
    
    - name: Install Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
    
    - name: Install Appium
      run: |
        npm install -g appium@next
        appium driver install uiautomator2
    
    - name: Start Appium Server
      run: appium &
    
    - name: Run Android Tests
      run: mvn clean test -Dplatform=android
    
    - name: Upload Test Reports
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: test-reports
        path: |
          target/surefire-reports/
          screenshots/
```

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    
    stages {
        stage('Setup') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        
        stage('Start Appium') {
            steps {
                sh 'appium &'
                sleep(5)
            }
        }
        
        stage('Run Tests') {
            steps {
                sh 'mvn test -Dplatform=android'
            }
        }
    }
    
    post {
        always {
            publishHTML([
                reportDir: 'target/surefire-reports',
                reportFiles: 'index.html',
                reportName: 'Test Report'
            ])
            archiveArtifacts artifacts: 'screenshots/**/*.png'
        }
    }
}
```

## Best Practices

### 1. Test Design
- ✅ Keep tests independent and isolated
- ✅ Use meaningful test and method names
- ✅ Follow the Arrange-Act-Assert pattern
- ✅ Implement Page Object Model for complex apps
- ✅ Use data providers for data-driven testing

### 2. Locator Strategies
- ✅ Prefer accessibility IDs over XPath
- ✅ Use resource IDs when available
- ✅ Avoid absolute XPath (use relative paths)
- ✅ Store locators as constants or in separate classes

### 3. Waits and Timeouts
- ✅ Always use explicit waits over implicit waits
- ✅ Never use `Thread.sleep()` - use WebDriverWait
- ✅ Set appropriate timeout values based on app performance
- ✅ Implement custom wait conditions for complex scenarios

### 4. Test Data Management
- ✅ Externalize test data (JSON, Excel, properties files)
- ✅ Use TestNG data providers for parameterization
- ✅ Keep sensitive data in environment variables
- ✅ Use test data generators for unique data

### 5. Reporting and Logging
- ✅ Log important test steps and decisions
- ✅ Capture screenshots for all failures
- ✅ Include meaningful assertions with messages
- ✅ Generate reports after every test run

### 6. Maintenance
- ✅ Run `mvn clean` regularly to clear old builds
- ✅ Update dependencies periodically
- ✅ Review and refactor test code regularly
- ✅ Keep framework documentation up to date

### 7. Performance
- ✅ Use `noReset: true` to avoid app reinstallation
- ✅ Run tests in parallel when possible
- ✅ Close unnecessary apps/processes during execution
- ✅ Use local Appium server for faster execution

## Advanced Features

### Parallel Test Execution
Update `testng.xml` for parallel execution:
```xml
<suite name="Parallel Suite" parallel="tests" thread-count="2">
    <test name="Android Test">
        <parameter name="platform" value="android"/>
        <classes>
            <class name="com.example.ExampleTest"/>
        </classes>
    </test>
    <test name="iOS Test">
        <parameter name="platform" value="ios"/>
        <classes>
            <class name="com.example.ExampleTest"/>
        </classes>
    </test>
</suite>
```

### Data-Driven Testing
```java
@DataProvider(name = "loginData")
public Object[][] getLoginData() {
    return new Object[][] {
        {"user1@test.com", "password1"},
        {"user2@test.com", "password2"},
        {"user3@test.com", "password3"}
    };
}

@Test(dataProvider = "loginData")
public void testLoginWithMultipleUsers(String email, String password) {
    // Test implementation
}
```

### Custom Capabilities
Add custom capabilities in `DriverManager.java`:
```java
caps.setCapability("autoGrantPermissions", true);
caps.setCapability("autoAcceptAlerts", true);
caps.setCapability("unicodeKeyboard", true);
caps.setCapability("resetKeyboard", true);
```

## Changelog

### Version 1.5.1 (2026-05-04)
- ✨ **Structure Alignment:** Moved `TestListener` to `src/main/java/listeners/` to match framework documentation.
- 🔧 **Logic Improvements:** Enhanced `ConfigReader` with regex-based environment variable resolution and better null safety.
- 🔧 **Utility Enhancements:** Implemented `scrollToElement` in `TestUtils` and added `By` locator support to `WaitHelper`.
- 🔧 **Base Class Refinement:** Updated `BaseTest` to default to "android" platform.
- 🎨 **Code Quality:** Applied Google Java Format across the entire codebase.

### Version 1.5.0 (2025-11-15)
- 🚀 **Final Release:** This version consolidates all recent fixes and improvements into a single, stable release.
- 🔧 **Dependency Resolution:** Resolved critical dependency conflicts between Appium (`9.3.0`) and Selenium (`4.19.0`) to ensure a stable build.
- 🔧 **Code Refactoring:**
  - Refactored `BaseTest` to use the centralized `DriverManager` for improved driver lifecycle management.
  - Updated `DriverManager` to use modern Appium 2.x `Options` and capabilities.
- 🐛 **Bug Fixes:**
  - Corrected `BaseTest` package declaration, import statements, and fixed a `NullPointerException` in `ConfigReader`.
- 🔧 **Project Maintenance:**
  - Corrected `.gitignore` filename and removed a redundant directory.
- 📚 **Documentation:** Updated `README.md` with a detailed changelog and troubleshooting steps.
- 🎨 **Formatting:** Applied the Google Java Format to the entire codebase for consistency.

### Version 1.4.0 (2025-11-15)
- 🔧 **Dependency Resolution:** Resolved critical dependency conflicts between Appium (`9.3.0`) and Selenium (`4.19.0`) by downgrading Selenium and excluding transitive dependencies. This fixes `NoClassDefFoundError` and improves stability.
- 🔧 **Code Refactoring:**
  - Refactored `BaseTest` to use the centralized `DriverManager` for improved driver lifecycle management and reduced code duplication.
  - Updated `DriverManager` to use modern Appium 2.x `Options` classes and ensured all capabilities are prefixed with `appium:`, aligning with current best practices.
- 🐛 **Bug Fixes:**
  - Corrected `BaseTest` package declaration and import statements to resolve compilation errors.
  - Fixed a `NullPointerException` in `ConfigReader` by aligning the configuration key for the Appium server.
- 🔧 **Project Maintenance:**
  - Corrected a typo in the `.gitignore` filename.
  - Removed a redundant `test` directory from the `src/main` folder to clean up the project structure.
- 📚 **Documentation:** Updated `README.md` with the latest changes and detailed troubleshooting steps for common environment issues.

### Version 1.3.0 (2025-11-15)
- ✨ Added `TestUtils` utility class with screenshot and interaction helpers
- ✨ Added `DriverException` for custom exception handling
- ✨ Added `TestListener` with automatic screenshot capture on failures
- ✨ Added comprehensive troubleshooting guide
- ✨ Added CI/CD integration examples (GitHub Actions, Jenkins)
- ✨ Added best practices and advanced features documentation
- 🔧 Fixed TestNG scope issue in `pom.xml` (removed `<scope>test</scope>`)
- 🔧 Updated dependency versions (Appium 9.3.0, Selenium 4.21.0)
- 🔧 Added Google Java Format plugin for code consistency
- 🔧 Enhanced `BaseTest` with better error handling
- 📚 Restructured documentation with clear sections
- 📚 Added example test code snippets
- 📚 Added detailed setup and verification steps

### Version 1.2.0 (2025-11-14)
- 🔧 Refactored `ConfigReader` to use constants and generic capability getter
- 🔧 Refactored `DriverManager` to streamline driver initialization
- 🔧 Refactored `WaitHelper` to load default timeout dynamically
- ✨ Introduced `BaseTest` class for driver setup and teardown
- 📝 Updated `ExampleTest` to extend `BaseTest`
- 📚 Updated documentation to reflect latest changes

### Version 1.1.0 (2025-11-13)
- 🔧 Refactored `GestureHelper` to use W3C Actions API (replacing deprecated `TouchAction`)
- ✨ Enhanced `TestListener` with completed screenshot functionality
- 🔧 Centralized driver management in `DriverManager`
- 📚 Added Javadoc comments for utilities and example tests
- 📚 Updated documentation with improvements and best practices

### Version 1.0.0 (2025-11-12)
- 🎉 Initial framework release
- ✨ Basic Appium + TestNG integration
- ✨ Android and iOS support
- ✨ Configuration management via JSON
- ✨ Basic utility classes

## Contributing

We welcome contributions! Please follow these guidelines:

### How to Contribute
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Run tests to ensure nothing breaks (`mvn test`)
5. Format code (`mvn fmt:format`)
6. Commit changes (`git commit -m 'Add amazing feature'`)
7. Push to branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Contribution Guidelines
- Write clear commit messages
- Add tests for new features
- Update documentation for significant changes
- Follow existing code style
- Ensure all tests pass before submitting PR

### Reporting Issues
- Use GitHub Issues for bug reports
- Include steps to reproduce
- Provide device/OS information
- Include relevant logs and screenshots

## Support

### Documentation
- [Appium Documentation](https://appium.io/docs/en/latest/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Selenium Documentation](https://www.selenium.dev/documentation/)

### Getting Help
- Open an issue for bugs or feature requests
- Check existing issues before creating new ones
- Join [Appium Discuss](https://discuss.appium.io/) for community support

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Made with ❤️ by Darshil**
