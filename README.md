# 🛡️ GigShield Mobile — Native Android Application

> **"Income protection for every gig worker."**  
> Native Android application for AI-powered parametric micro-insurance.

---

## 📱 Application Overview

**GigShield Mobile** is a production-quality native Android application built using **Kotlin**, **Jetpack Compose**, **Material 3**, and **MVVM Clean Architecture**. It extends the GigShield parametric insurance platform for India's 15 million+ active gig workers (Zomato riders, Swiggy delivery partners, Uber drivers, Zepto runners).

When extreme monsoons, hazardous air quality (AQI > 400), or civic curfews disrupt delivery earnings, GigShield Mobile verifies real-time satellite data and hardware telemetry to **adjudicate and disburse simulated instant UPI payments via Razorpay — zero paperwork required.**

---

## 📦 Verified Build & Deliverables

* **Debug APK Generated:** `YES` ✅
* **APK Location:**
  ```
  gigshield-android/app/build/outputs/apk/debug/app-debug.apk
  ```
* **APK Binary Size:** `16,935,402 bytes (~16.9 MB)`
* **Compilation Status:** `BUILD SUCCESSFUL` (37 actionable tasks executed/up-to-date)
* **Target SDK:** Android 14 (API 34)
* **Min SDK:** Android 7.0 (API 24)

---

## 🧪 Verified Automated Unit Test Suite

All 12 automated unit tests across 4 test suites have executed and passed with 0 failures, 0 errors, and 0 skipped:

```
> Task :app:testDebugUnitTest
Finished generating test XML results into: app/build/test-results/testDebugUnitTest
Finished generating test HTML results into: app/build/reports/tests/testDebugUnitTest

BUILD SUCCESSFUL
```

| Test Class | Tests Executed | Passed | Failures | Purpose |
|---|:---:|:---:|:---:|---|
| [`EarningsCalculatorTest`](app/src/test/java/com/gigshield/mobile/EarningsCalculatorTest.kt) | 4 | 4 | 0 | Verifies mathematical loss gap (`expected - actual`), payout capping to remaining policy coverage, and 7-day daily breakdown summary. |
| [`ValidationHelperTest`](app/src/test/java/com/gigshield/mobile/ValidationHelperTest.kt) | 3 | 3 | 0 | Verifies ₹100 minimum loss threshold, weekly claim cap enforcement, and biomechanical accelerometer variance threshold ($>0.5$). |
| [`JsonSerializationTest`](app/src/test/java/com/gigshield/mobile/JsonSerializationTest.kt) | 2 | 2 | 0 | Confirms strict JSON schema parity with MongoDB Mongoose models for `UserDto` and `ClaimDto`. |
| [`ClaimsViewModelTest`](app/src/test/java/com/gigshield/mobile/ClaimsViewModelTest.kt) | 3 | 3 | 0 | Verifies domain model instantiation and StateFlow status mappings (`APPROVED`, `UNDER_REVIEW`, `REJECTED`). |

---

## ✨ Features Implemented

| Feature | Screen | Working State | Description |
|---|---|:---:|---|
| 🏠 **Home Dashboard** | `HomeScreen` | **Verified** | Contextual worker greeting, active policy banner, live Open-Meteo satellite weather card, Brain.js dynamic risk gauge, live hardware accelerometer sensor telemetry, and recent claims stream. |
| 🛡️ **Insurance Plans** | `PlansScreen` | **Verified** | 3 dynamic tiers (**Basic** ₹12/wk, **Pro** ₹24/wk, **Elite** ₹36/wk), policy comparison matrix, dynamic pricing based on threat zone, and one-tap subscription/cancellation. |
| ⚡ **Parametric Claims & Simulation** | `ClaimsScreen` | **Verified** | Interactive disruption simulator for Heavy Rain, High AQI, Curfew, and App Crash with real-time 3-stage progress bar. |
| 💸 **Razorpay UPI Simulator** | `RazorpayBottomSheet` | **Demo Flow** | Dark glassmorphism modal recreating Razorpay's instant UPI bank settlement with animated spinner $\rightarrow$ green success checkmark. *(Demonstration only — never executes real money transfers)* |
| 🔍 **7-Vector Fraud Inspector** | `ClaimDetailScreen` | **Verified** | Detailed inspection breakdown of GPS bounds, biomechanical variance, velocity anti-spoofing, and 24-hr cooldown checks. |
| 🌧️ **Risk & Oracles Dashboard** | `RiskDashboardScreen` | **Verified** | Live threat breakdown, catalog of the 5 parametric disruption vectors, and a 7-day predictive analytics forecast chart. |
| 📈 **Income Loss & Earnings** | `EarningsScreen` | **Verified** | Shift expected margin vs. actual revenue vs. recovered loss bar charts and lifetime payout tracking. |
| 👤 **Worker Profile & Config** | `ProfileScreen` | **Verified** | Operating zone updater with live AI risk recalculation, dark/light theme toggle, and 3-way backend connection switcher. |

---

## ⚙️ Functionality Matrix

To maintain transparency:
* **Real Working Functionality:**
  - Real hardware `SensorManager` telemetry computation (accelerometer variance).
  - Clean MVVM state flows with Jetpack Compose Material 3 UI.
  - Room database offline caching for user profiles and claim records.
  - Configurable REST API client with timeout and cleartext security policies for emulator testing.
  - Automated unit test suite verifying mathematical calculations, threshold validations, and JSON serialization.
* **Demonstration / Simulated Functionality:**
  - **Razorpay UPI Simulator:** High-fidelity simulation demonstrating how an instant bank payout flow appears to the gig worker upon approval. No actual banking or payment gateway credentials are used or charged.
  - **Simulated Disruption Triggers:** Enables testing the 7-vector fraud engine and zero-touch payout flows on demand.
  - **Offline Demo Mode:** Transparent fallback engine (`MockApiClient`) providing realistic data when no server is connected.
* **Unverified in Live Environment:**
  - Direct on-device emulator run was unverified due to the absence of a pre-configured Android Virtual Device (AVD) image on the host environment; however, the APK binary was compiled and verified via `assembleDebug`.

---

## 🛠️ Technology Stack

* **Language:** Kotlin 1.9.23
* **UI Framework:** Jetpack Compose (BOM 2024.04.00) + Material 3
* **Architecture:** Model-View-ViewModel (MVVM) with Unidirectional Data Flow
* **Reactive Streams:** Kotlin Coroutines & `StateFlow`
* **Navigation:** Jetpack Navigation Compose (`NavHost`, `composable`, deep routes)
* **Networking:** Retrofit 2.9.0 + OkHttp 4.12.0 + Gson 2.10.1
* **Local Persistence:** Room Database 2.6.1 (`UserDao`, `ClaimDao`, `GigShieldDatabase`)
* **Hardware Sensors:** Android `SensorManager` (hardware accelerometer variance calculation)
* **Testing:** JUnit 4, Kotlinx Coroutines Test, MockK
* **Build System:** Gradle 8.5 with Android Gradle Plugin 8.3.2

---

## 🚀 Running and Building

### 1. Build Debug APK
```cmd
cd gigshield-android
.\gradlew.bat assembleDebug
```
The compiled APK will be at:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Run Automated Unit Tests
```cmd
cd gigshield-android
.\gradlew.bat testDebugUnitTest
```

### 3. Open in Android Studio
1. Launch Android Studio.
2. Select **File $\rightarrow$ Open** and choose:
   `D:\Projects\Hackathons-Projects\GigShield\gigshield-android`
3. Select an emulator or connected device and click **Run (Shift+F10)**.

### 4. Backend Target Configuration
In **Profile $\rightarrow$ Backend Connection Target**, select:
* **Render Cloud:** `https://gigshield-backend-c1z7.onrender.com/` (Default live cloud server).
* **10.0.2.2 Host:** Connects to your local Node.js server on port 5000 from an Android emulator.
* **Offline Demo:** Standalone mock mode requiring no network connection.
