# Build Instructions

This Android application can be built using Android Studio or command line with Gradle.

## Prerequisites

1. **Android Studio** (Recommended): Arctic Fox (2020.3.1) or later
   - Download from: https://developer.android.com/studio

2. **Java Development Kit (JDK)**: Version 8 or 11
   - Download from: https://adoptium.net/

3. **Android SDK**: API Level 33 (Android 13)
   - Can be installed through Android Studio

## Building with Android Studio

1. **Open Project**
   - Launch Android Studio
   - Click "Open an Existing Project"
   - Navigate to the Repoone directory and select it

2. **Sync Gradle**
   - Android Studio will automatically start syncing Gradle
   - Wait for the sync to complete (this may take a few minutes on first run)

3. **Build APK**
   - From the menu: Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for the build to complete
   - Click "locate" in the notification to find the APK

4. **Alternative: Generate Signed APK** (for release)
   - From the menu: Build → Generate Signed Bundle / APK
   - Follow the wizard to create or select a keystore
   - Choose "release" build variant
   - Click Finish

## Building with Command Line

### On Linux/Mac:

```bash
# Make sure gradlew is executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Output location
# Debug: app/build/outputs/apk/debug/app-debug.apk
# Release: app/build/outputs/apk/release/app-release-unsigned.apk
```

### On Windows:

```cmd
# Build debug APK
gradlew.bat assembleDebug

# Build release APK
gradlew.bat assembleRelease
```

## Installing the APK

### On Physical Device:

1. **Enable Developer Options**
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   
2. **Enable USB Debugging**
   - Go to Settings → Developer Options
   - Enable "USB Debugging"

3. **Install via ADB**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

4. **Or Transfer APK**
   - Copy the APK to your device
   - Enable "Install from Unknown Sources" for your file manager
   - Open the APK file and tap Install

### On Emulator:

1. **Create Emulator in Android Studio**
   - Tools → AVD Manager → Create Virtual Device
   - Select a device definition (e.g., Pixel 5)
   - Select system image (API 33 recommended)
   - Finish and launch the emulator

2. **Install via Android Studio**
   - With emulator running, click "Run" (green play button)
   - Select the running emulator
   - App will be installed and launched automatically

3. **Or Install via ADB**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## Troubleshooting

### Gradle sync fails
- Check internet connection (Gradle downloads dependencies)
- Try: File → Invalidate Caches / Restart
- Delete `.gradle` folder and retry

### Build fails
- Ensure you have the correct SDK version (API 33)
- Check that JDK 8 or 11 is installed
- Try: Build → Clean Project, then rebuild

### APK won't install
- Check that "Install from Unknown Sources" is enabled
- Ensure the device meets minimum SDK requirements (API 24 / Android 7.0)
- Check for conflicting app signatures if reinstalling

## File Locations

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`
- **Logs**: `app/build/outputs/logs/`

## Additional Commands

```bash
# Clean build
./gradlew clean

# List all tasks
./gradlew tasks

# Run tests
./gradlew test

# Check for dependency updates
./gradlew dependencyUpdates
```
