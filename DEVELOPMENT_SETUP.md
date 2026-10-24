# GigShield Mobile — Development Setup & Execution Guide

---

## 1. Prerequisites

* **Operating System:** Windows 10/11, macOS, or Linux.
* **JDK:** Java Development Kit 17 or higher (JDK 20 is verified and supported).
* **Android Studio:** Android Studio Hedgehog (2023.1.1) / Iguana / Jellyfish or later.
* **Android SDK:** Platform 34 (Android 14) and build-tools `34.0.0`.
* **Gradle:** Gradle 8.5 (configured via the provided `gradlew` wrapper).

---

## 2. Opening in Android Studio

1. Launch Android Studio.
2. Select **File $\rightarrow$ Open...**
3. Navigate to `d:\Projects\Hackathons-Projects\GigShield\gigshield-android` and click **OK**.
4. Allow Gradle Sync to finish downloading dependencies.

---

## 3. Running on Emulator / Physical Device

### Using Android Studio
1. Select the **app** configuration in the top toolbar.
2. Select an Android Virtual Device (AVD) running API 26 or higher (API 34 recommended).
3. Click the green **Run (Shift+F10)** button.

### Using Command Line
* **Windows:**
  ```cmd
  cd gigshield-android
  gradlew.bat assembleDebug
  gradlew.bat installDebug
  ```
* **macOS / Linux:**
  ```bash
  cd gigshield-android
  ./gradlew assembleDebug
  ./gradlew installDebug
  ```

The generated debug APK will be located at:
```
gigshield-android/app/build/outputs/apk/debug/app-debug.apk
```

---

## 4. Connecting to the GigShield Backend

The app supports 3 connection modes configurable in **Profile $\rightarrow$ Backend Connection Target**:

1. **Cloud Production (Default):**
   - Automatically connects to `https://gigshield-backend-c1z7.onrender.com/`.
   - Requires no local server setup.
2. **Localhost on Android Emulator:**
   - Run the Node.js backend locally:
     ```cmd
     cd backend
     npm start
     ```
   - In the mobile app, tap **10.0.2.2 Host**. The app will connect directly to `http://10.0.2.2:5000/`.
3. **Standalone Offline Demo Mode:**
   - Tap **Offline Demo**. The app will route requests through `MockApiClient` with full instant responses.

---

## 5. Running Automated Unit Tests

Execute the unit test suite covering loss calculations, JSON parsing, and ViewModel states:
```cmd
cd gigshield-android
gradlew.bat testDebugUnitTest
```
Or in Android Studio:
Right-click `app/src/test/java/com/gigshield/mobile` and select **Run 'Tests in 'com.gigshield.mobile''**.
