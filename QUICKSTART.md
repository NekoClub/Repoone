# Quick Start Guide

Get Repoone up and running in minutes!

## For Users

### Installation

1. **Download APK**
   - Get the latest APK from [Releases](https://github.com/NekoClub/Repoone/releases)
   - Or build from source (see below)

2. **Install on Device**
   - Enable "Install from Unknown Sources" in Settings
   - Open the APK file
   - Tap "Install"

3. **First Launch**
   - Open Repoone
   - Set a 4-6 digit PIN
   - Confirm your PIN
   - Start adding images!

### Quick Usage

**Add Images**
```
Tap + → Choose Gallery or Camera → Select image → Done!
```

**Edit Images**
```
Long-press image → Edit Image → Use controls → Save
```

**Share Images**
```
Long-press image → Share Image → Choose app
```

**Change PIN**
```
Tap ⚙️ → Change PIN → Enter old and new PIN → Save
```

---

## For Developers

### Build from Source

#### Prerequisites
```bash
# Required
- Android Studio Arctic Fox+
- JDK 8 or 11
- Android SDK (API 33)
```

#### Clone & Build
```bash
# 1. Clone repository
git clone https://github.com/NekoClub/Repoone.git
cd Repoone

# 2. Open in Android Studio
# File → Open → Select Repoone directory

# 3. Sync Gradle (automatic)
# Wait for sync to complete

# 4. Build APK
# Build → Build Bundle(s) / APK(s) → Build APK(s)

# Or via command line:
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

### Project Structure at a Glance

```
Repoone/
├── app/src/main/
│   ├── java/com/nekoclub/repoone/
│   │   ├── LauncherActivity.kt     # Entry point
│   │   ├── AuthActivity.kt         # PIN auth
│   │   ├── MainActivity.kt         # Vault UI
│   │   ├── ImageEditorActivity.kt  # Editor
│   │   ├── SettingsActivity.kt     # Settings
│   │   ├── VaultManager.kt         # Storage
│   │   ├── SecurePreferences.kt    # Encryption
│   │   └── ImageAdapter.kt         # Grid UI
│   └── res/                        # Resources
├── docs/                           # Documentation
└── README.md                       # Main docs
```

### Key Components

| Component | Purpose | File |
|-----------|---------|------|
| LauncherActivity | Entry point | LauncherActivity.kt |
| AuthActivity | PIN authentication | AuthActivity.kt |
| MainActivity | Vault grid view | MainActivity.kt |
| ImageEditorActivity | Edit images | ImageEditorActivity.kt |
| VaultManager | Image storage | VaultManager.kt |
| SecurePreferences | Encrypted PIN | SecurePreferences.kt |

### Common Development Tasks

#### Run on Emulator
```bash
# Start emulator
# Tools → AVD Manager → Launch emulator

# Run app
./gradlew installDebug

# Or use Android Studio Run button
```

#### Run on Physical Device
```bash
# Enable USB debugging on device
# Connect via USB

# Install
adb install app/build/outputs/apk/debug/app-debug.apk

# Or use Android Studio
```

#### View Logs
```bash
# Real-time logs
adb logcat | grep Repoone

# Or use Android Studio Logcat window
```

#### Clean Build
```bash
./gradlew clean
./gradlew assembleDebug
```

### Adding New Features

#### 1. New Activity
```kotlin
// Create Activity class
class NewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
    }
}

// Add to AndroidManifest.xml
<activity
    android:name=".NewActivity"
    android:exported="false" />

// Create layout: res/layout/activity_new.xml
```

#### 2. New Feature in Existing Activity
```kotlin
// Add function to activity
private fun newFeature() {
    // Implementation
}

// Add button to layout
<Button
    android:id="@+id/btnNewFeature"
    android:text="New Feature" />

// Wire up in onCreate
findViewById<Button>(R.id.btnNewFeature).setOnClickListener {
    newFeature()
}
```

### Testing

#### Manual Testing Checklist
- [ ] PIN setup works
- [ ] PIN unlock works
- [ ] Gallery import works
- [ ] Camera capture works
- [ ] Image editing works (rotate, filters)
- [ ] Save changes works
- [ ] Share to other apps works
- [ ] Delete with confirmation works
- [ ] Change PIN works
- [ ] App locks on exit

#### Debug Tips
```kotlin
// Add logging
import android.util.Log

Log.d("Repoone", "Debug message")
Log.e("Repoone", "Error message", exception)

// Breakpoints in Android Studio
// Click line number gutter → Run in Debug mode
```

### Common Issues & Solutions

#### Gradle Sync Fails
```bash
# Solution 1: Check internet connection
# Solution 2: Invalidate caches
# File → Invalidate Caches / Restart

# Solution 3: Delete .gradle folder
rm -rf .gradle
./gradlew clean
```

#### Build Fails
```bash
# Check JDK version
java -version  # Should be 8 or 11

# Check SDK installation
# Android Studio → SDK Manager → Install API 33

# Clean and rebuild
./gradlew clean assembleDebug
```

#### App Crashes
```bash
# View crash logs
adb logcat | grep -i exception

# Check in Android Studio Logcat
# Filter by "Error" and "Exception"
```

### Code Style

#### Formatting
```kotlin
// Good
class VaultManager(private val context: Context) {
    fun importImage(inputStream: InputStream): VaultImage {
        // Implementation
    }
}

// Bad
class VaultManager(private val context: Context){
  fun importImage(inputStream: InputStream):VaultImage{
    // Implementation
  }
}
```

#### Naming
- Classes: PascalCase (VaultManager)
- Functions: camelCase (importImage)
- Variables: camelCase (imageFile)
- Constants: UPPER_SNAKE_CASE (MAX_IMAGES)

### Resources

#### Documentation
- [Full README](README.md)
- [Features](FEATURES.md)
- [Architecture](ARCHITECTURE.md)
- [Build Instructions](BUILD_INSTRUCTIONS.md)
- [Contributing](CONTRIBUTING.md)

#### External Resources
- [Kotlin Docs](https://kotlinlang.org/docs/home.html)
- [Android Developers](https://developer.android.com/)
- [Material Design](https://material.io/design)

### Getting Help

- **Issues**: [GitHub Issues](https://github.com/NekoClub/Repoone/issues)
- **Discussions**: [GitHub Discussions](https://github.com/NekoClub/Repoone/discussions)
- **Contributing**: See [CONTRIBUTING.md](CONTRIBUTING.md)

---

## Quick Commands Reference

```bash
# Build
./gradlew assembleDebug         # Build debug APK
./gradlew assembleRelease       # Build release APK
./gradlew clean                 # Clean build files

# Install
adb install <path-to-apk>       # Install on device
adb uninstall com.nekoclub.repoone  # Uninstall

# Debugging
adb logcat                      # View all logs
adb logcat | grep Repoone       # Filter logs
adb devices                     # List connected devices
adb shell pm list packages      # List installed packages

# Gradle
./gradlew tasks                 # List all tasks
./gradlew dependencies          # Show dependencies
./gradlew build                 # Full build
```

---

## Version Information

- **Current Version**: 1.0.0
- **Min Android**: 7.0 (API 24)
- **Target Android**: 13 (API 33)
- **Language**: Kotlin 1.8.0
- **Build Tools**: Gradle 7.5

---

## Need More Help?

Check out the comprehensive documentation:
- 📖 [README](README.md) - Overview and basic usage
- 🏗️ [ARCHITECTURE](ARCHITECTURE.md) - Technical details
- 🚀 [BUILD_INSTRUCTIONS](BUILD_INSTRUCTIONS.md) - Detailed build guide
- ✨ [FEATURES](FEATURES.md) - Feature documentation
- 🤝 [CONTRIBUTING](CONTRIBUTING.md) - Contribution guidelines
