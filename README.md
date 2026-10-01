# Web View and Menus Android Application

A modern Android application built using **Jetpack Compose** and **Material 3** that demonstrates the integration of an embedded **WebView** along with interactive **Option Menus**, quick access bookmarks, and navigation controls.

---

## 🚀 Features

- **Embedded WebView**: Renders web content seamlessly using Compose `AndroidView` with JavaScript and DOM storage enabled.
- **TopAppBar Options Menu**: 
  - Dropdown menu (`DropdownMenu`) featuring quick preset bookmarks.
  - Option to clear web cache and browser history.
  - "About Application" modal dialog.
- **Quick Access Bookmark Chips**: Horizontal scrollable chips (`AssistChip`) for single-tap navigation to popular websites (Google, Wikipedia, GitHub, Android Developers, Stack Overflow).
- **Smart URL Address Bar**:
  - Outlined text field supporting direct web URLs or general search queries.
  - Auto-formats input (prepending `https://` if missing or routing search terms to Google Search).
  - Clear input button & soft keyboard `ImeAction.Go` integration.
- **Page Load Progress**: Real-time `LinearProgressIndicator` indicating web page load progress.
- **System Back Navigation**: Custom `BackHandler` that intercepts the Android device back button to navigate backwards in the `WebView` history before closing the application.

---

## 🛠️ Tech Stack & Requirements

- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Minimum SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 37
- **Permissions**: `android.permission.INTERNET`

---

## 📁 Key File Structure

```text
exp8/
├── app/
│   ├── build.gradle.kts                      # Module build configuration & dependencies
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml           # Manifest with INTERNET permission
│           ├── java/com/example/exp8/
│           │   ├── MainActivity.kt           # Main UI logic (WebView, Menus, TopAppBar)
│           │   └── ui/theme/                 # App Color, Theme & Typography definitions
│           └── res/
│               └── values/
│                   └── strings.xml           # Application string resources
└── README.md
```

---

## 📥 How to Run

1. Open **Android Studio**.
2. Select **Open** and select the project directory `exp8`.
3. Sync the project with Gradle files (`Gradle Sync`).
4. Select an Android Emulator or connected physical device running Android 7.0 (API 24) or higher.
5. Click **Run ('app')** (`Shift + F10`).
