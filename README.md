# 🚀 SauceDemo Enterprise Automation Framework

[![CI Automated Test Suite](https://github.com/<USERNAME>/<REPO>/actions/workflows/ci.yml/badge.svg)](https://github.com/<USERNAME>/<REPO>/actions/workflows/ci.yml)

An industry-standard, production-ready UI Test Automation Framework built with **Java 21**, **Selenium 4**, **TestNG 7**, and **Allure Reporting**, automating end-to-end user workflows on [SauceDemo](https://www.saucedemo.com).

Designed with clean architecture, Page Object Model (POM), reusable component design, ThreadLocal multi-browser concurrency, and CI/CD integration.

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| **Java** | 21 (LTS) | Core Programming Language |
| **Selenium WebDriver** | 4.49.0 | Browser Automation & Selenium Manager |
| **TestNG** | 7.10.2 | Test Runner, Assertions, Parallel Execution, Data Providers |
| **Allure Report** | 2.29.0 | Interactive Visual Test Reporting & Step Attachments |
| **Maven** | 3.9+ | Build, Dependency & Lifecycle Management |
| **Log4j2** | 2.24.1 | Structured Console & Rolling File Logging |
| **Jackson** | 2.17.2 | JSON Test Data Parsing |
| **Apache POI** | 5.3.0 | Excel Spreadsheet Data-Driven Testing |
| **GitHub Actions** | CI/CD | Headless Regression Pipeline & Artifact Publishing |

---

## 🏗️ Framework Architecture

```
SELENIUM AUTOMATION PROJECT/
├── .github/
│   └── workflows/
│       └── ci.yml                     # GitHub Actions CI pipeline for headless test execution
├── src/
│   ├── main/
│   │   ├── java/com/company/framework/
│   │   │   ├── base/
│   │   │   │   ├── BasePage.java       # Base Page Object with fluent waits, actions & masked inputs
│   │   │   │   └── BaseTest.java       # TestNG lifecycle setup, teardown & ThreadLocal cleanup
│   │   │   ├── config/
│   │   │   │   ├── ConfigReader.java   # Config loader with CLI (-D) & env variable overrides
│   │   │   │   └── Environment.java    # Supported environment definitions
│   │   │   ├── driver/
│   │   │   │   ├── DriverFactory.java  # Native Selenium Manager factory (Chrome, Firefox, Edge)
│   │   │   │   └── DriverManager.java  # ThreadLocal WebDriver storage for safe parallel runs
│   │   │   ├── exceptions/
│   │   │   │   └── FrameworkException.java # Custom runtime exception wrapper
│   │   │   ├── listeners/
│   │   │   │   ├── Flaky.java          # Custom annotation for targeting flaky tests
│   │   │   │   ├── RetryAnalyzer.java  # Selective test retry mechanism for @Flaky methods
│   │   │   │   └── TestListener.java   # TestNG listener capturing Allure failure screenshots & logs
│   │   │   ├── pages/
│   │   │   │   ├── CartPage.java       # Cart page operations & validations
│   │   │   │   ├── CheckoutCompletePage.java # Order confirmation page
│   │   │   │   ├── CheckoutInfoPage.java     # Customer information entry step
│   │   │   │   ├── CheckoutOverviewPage.java # Order overview, tax & total calculation
│   │   │   │   ├── HeaderComponent.java      # Shared navbar, cart badge & menu component
│   │   │   │   ├── InventoryPage.java        # Products catalog & inventory actions
│   │   │   │   ├── LoginPage.java            # Login authentication actions & error parsing
│   │   │   │   └── ProductSortPage.java      # Catalog sorting & filtering operations
│   │   │   └── utils/
│   │   │       ├── DateUtils.java      # Timestamp formatting utilities
│   │   │       ├── ExcelUtils.java     # Apache POI spreadsheet reader
│   │   │       ├── JsonUtils.java      # Jackson JSON reader
│   │   │       ├── RandomDataUtils.java # Dynamic test data generation
│   │   │       ├── ScreenshotUtils.java # File & byte screenshot capture for reports
│   │   │       └── WaitUtils.java      # Fluent explicit wait utilities
│   │   └── resources/
│   │       ├── config/
│   │       │   ├── config.properties   # Central framework configuration
│   │       │   ├── dev.properties      # Development environment properties
│   │       │   ├── qa.properties       # QA environment properties
│   │       │   ├── staging.properties  # Staging environment properties
│   │       │   └── prod.properties     # Production environment properties
│   │       └── log4j2.xml              # Logging layout & rolling file appender configuration
│   └── test/
│       ├── java/com/company/tests/
│       │   ├── CartTests.java          # Shopping cart test scenarios
│       │   ├── CheckoutTests.java      # End-to-end checkout & price computation tests
│       │   ├── InventoryTests.java     # Product catalog display & count tests
│       │   ├── LoginTests.java         # Authentication & DataProvider error tests
│       │   ├── LogoutTests.java        # Session termination & route protection tests
│       │   └── SearchTests.java        # Catalog sorting & price ordering tests
│       └── resources/
│           └── testdata/
│               ├── checkout.json       # Negative checkout test scenarios
│               ├── login.json          # Negative login credentials test scenarios
│               ├── products.json       # Product catalog baseline data
│               └── sample.xlsx         # Sample Excel test dataset
├── pom.xml                             # Maven project descriptor & dependency management
├── testng.xml                          # Default parallel execution suite (methods, thread-count=3)
├── testng-smoke.xml                    # Smoke test suite
├── testng-regression.xml               # Regression test suite
├── testng-parallel.xml                 # High-throughput parallel test suite
└── testng-crossbrowser.xml             # Cross-browser suite (Chrome & Firefox)
```

### Architectural Highlights

1. **Page Object Model (POM) with Component Extraction**: Page interactions are cleanly decoupled into distinct classes. Shared elements (Navbar, Burger Menu, Cart Badge) are encapsulated in `HeaderComponent` to eliminate code duplication.
2. **ThreadLocal Concurrency**: `DriverManager` maintains isolated `ThreadLocal<WebDriver>` instances, allowing test methods to execute in parallel without session interference.
3. **Selenium Manager Native Driver Handling**: Eliminates external driver binaries or third-party managers; uses modern Selenium Manager built into Selenium 4.x.
4. **Security & Sensitive Data Masking**: Passwords and secrets are typed using `typeMasked()`, logging `****` in console and Allure steps.
5. **Zero Hardcoded Sleeps & Instant Absences**: Dynamic explicit waits (`WaitUtils`) are used everywhere. Non-blocking presence checks (`findElements().isEmpty()`) prevent unnecessary timeouts when checking element absence.
6. **Smart Flaky Test Retries**: `RetryAnalyzer` specifically targets only `@Flaky` annotated tests instead of blindly rerunning the whole suite.

---

## ✨ Key Features

- **Multi-Browser & Headless Support**: Seamless switching between Chrome, Firefox, and Edge via config or CLI flags.
- **Hierarchical Configuration Priority**: System properties (`-D`) > Environment Variables > `config.properties` > Default fallbacks.
- **Data-Driven Testing (DDT)**: Tests consume JSON via Jackson or Excel via Apache POI.
- **Rich Allure Reporting**: Detailed step logs, environment parameters, and automatic screenshot attachments on test failure.
- **CI/CD Ready**: Automated GitHub Actions workflow running headless tests on push and pull requests with downloadable reports.

---

## 📋 Prerequisites

- **Java JDK 21+** installed and configured on `PATH` (`java -version`).
- **Apache Maven 3.9+** installed (`mvn -version`).
- **Google Chrome**, **Mozilla Firefox**, or **Microsoft Edge** browser installed.
- *(Optional)* **Allure CLI** installed (`allure --version`) for local report viewing.

---

## 🚦 How to Run Tests

### 1. Run Headless Suite (Recommended for CI / Terminal)
```bash
mvn clean test -Dheadless=true
```

### 2. Run Full Test Suite in Browser (Default parallel execution)
```bash
mvn clean test
```

### 3. Run Smoke Tests Only
```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
# Or using TestNG group filter:
mvn test -Dgroups=smoke
```

### 4. Run Regression Tests
```bash
mvn test -DsuiteXmlFile=testng-regression.xml
```

### 5. Run Cross-Browser Tests (Parallel by test)
```bash
mvn test -DsuiteXmlFile=testng-crossbrowser.xml
```

### 6. CLI Overrides (Browser, Headless Mode, Environment)
```bash
# Run on Firefox in Headless Mode
mvn clean test -Dbrowser=firefox -Dheadless=true

# Run on Edge against Staging environment
mvn clean test -Dbrowser=edge -Denv=staging -Dheadless=true

# Run with custom explicit wait timeout (15 seconds)
mvn clean test -Dtimeout=15
```

---

## 📊 Allure Reporting

Generate and open the Allure HTML report locally:

```bash
# Serve real-time report in browser
allure serve target/allure-results

# Or generate static HTML report via Maven
mvn allure:report
mvn allure:serve
```

---

## 📸 Screenshots

> _Placeholder: Add an Allure report screenshot or execution dashboard image here later after generating your report (e.g., `![Allure Report Dashboard](docs/allure-dashboard.png)`)._

---

## 🧪 Automated Test Cases Matrix

| Test ID | Class Name | Method Name | Group | Scenario / Objective | Expected Result |
|---|---|---|---|---|---|
| **AUTH-01** | `LoginTests` | `validLoginTest` | `smoke`, `regression` | Valid login with standard credentials | Navigates to `/inventory.html`, header is "Products" |
| **AUTH-02** | `LoginTests` | `invalidLoginTest` [x4] | `regression` | Data-driven invalid logins (locked user, wrong password, empty fields) | Displays exact error message matching each test case |
| **AUTH-03** | `LogoutTests` | `logoutSuccessfully` | `smoke`, `regression` | Log out via side navigation menu | Redirects to login page, login button is displayed |
| **AUTH-04** | `LogoutTests` | `cannotAccessInventoryAfterLogout` | `regression` | Direct URL navigation to `/inventory.html` post-logout | Access is denied with error message |
| **INV-01** | `InventoryTests` | `pageTitleValidationTest` | `smoke`, `regression` | Verify application window title | Window title equals `"Swag Labs"` |
| **INV-02** | `InventoryTests` | `productCountIsSix` | `smoke`, `regression` | Verify total catalog count | Exactly 6 product items are rendered |
| **INV-03** | `InventoryTests` | `allProductsHavePriceAndName` | `regression` | Validate data integrity across catalog items | Every product has a non-blank title and price $> 0.0$ |
| **SORT-01** | `SearchTests` | `sortPriceLowToHighTest` | `regression` | Select 'Price (low to high)' dropdown | Prices sorted in strictly ascending order |
| **SORT-02** | `SearchTests` | `sortNameZToATest` | `regression` | Select 'Name (Z to A)' dropdown | Product titles sorted in reverse alphabetical order |
| **CART-01** | `CartTests` | `addSingleProductToCart` | `smoke`, `regression` | Add single item from catalog | Cart badge shows `1`, Cart page lists exact added product |
| **CART-02** | `CartTests` | `addMultipleProductsToCart` | `regression` | Add two different products | Cart badge shows `2`, Cart page lists both exact products |
| **CART-03** | `CartTests` | `removeProductFromCartOnHomePage` | `regression` | Remove item directly on inventory page | Cart badge removes immediately ($0$ wait time) |
| **CART-04** | `CartTests` | `removeProductFromCartPage` | `regression` | Remove item from within Cart page | Cart becomes empty and badge disappears |
| **CHK-01** | `CheckoutTests` | `completeCheckoutEndToEnd` | `smoke`, `regression` | Complete full purchase journey | "Thank you for your order!" confirmation header displayed |
| **CHK-02** | `CheckoutTests` | `checkoutWithMissingInformation` [x3] | `regression` | Data-driven missing fields in checkout form | Accurate validation error per missing field |
| **CHK-03** | `CheckoutTests` | `cancelCheckoutReturnsToCart` | `regression` | Click Cancel on checkout step 1 | URL contains `/cart.html` and cart page is restored |
| **CHK-04** | `CheckoutTests` | `checkoutOverviewTotalIsCorrect` | `regression` | Validate checkout summary math | Subtotal + Tax precisely equals Total parsed as doubles |

---

## 🔮 Future Improvements

- [ ] **Docker & Selenium Grid Integration**: Containerize test execution using Docker Compose and Zalenium/Selenium Grid.
- [ ] **BDD Cucumber Integration**: Add Gherkin `.feature` specifications for enhanced business stakeholder collaboration.
- [ ] **REST API Hybrid Tests**: Seed test state using backend API calls before executing UI assertions to increase test velocity.
- [ ] **Visual Regression Testing**: Integrate Percy or Applitools for automated visual diff verification.
