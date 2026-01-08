# Repoone - Secure Vault & Image Editor

An Android application that provides secure image storage with PIN protection, built-in image editing capabilities, and seamless social media sharing.

> **⚠️ HARDLINE SECURITY MODE ENABLED**
>
> This vault implements aggressive security measures for maximum protection:
> - **6-digit PIN** with complexity requirements (no sequential/repeated digits)
> - **Progressive lockouts** after failed attempts (10s → 30s → 2min)
> - **Automatic vault wipe** after 5 failed PIN attempts (permanent data loss)
> - **Mandatory PIN changes** every 30 days with persistent reminders
> - **No PIN recovery option** - forgotten PINs result in permanent data loss
> - **Screenshot/screen recording disabled** throughout the app
> - **Anti-debugging protection** prevents tampering
>
> These features prioritize security over convenience. See [EVIL_FEATURES.md](EVIL_FEATURES.md) for detailed documentation.

## Features

### 🔒 Hardline Security Vault
- **Ruthless PIN authentication** with progressive lockouts and auto-wipe
- **Complex PIN requirements** (6+ digits, no simple patterns)
- **Forced PIN expiry** every 30 days
- Encrypted preferences using Android Security Crypto (AES-256-GCM)
- Private storage for sensitive images
- Screenshot and screen recording prevention
- Anti-debugging and tampering detection
- Comprehensive access logging

### 🎨 Image Editor
- **Rotate**: Rotate images 90 degrees
- **Filters**: Apply brightness, contrast, and saturation adjustments
- **Real-time preview**: See changes as you adjust

### 📤 Social Media Sharing
- Share images directly to social media platforms
- Supports any app that accepts image sharing via Android's share intent
- Includes: Facebook, Instagram, Twitter, WhatsApp, and more
- **NEW:** Auto intent assignment - receive images from other apps

### 📸 Import Options
- Import from gallery
- Take photos with camera
- Automatic secure storage
- **NEW:** Share images directly to vault from any app

### 🌐 Overlay System (NEW)
- Floating button visible over all apps
- Drag-and-drop positioning
- Quick access to vault from anywhere
- Expandable control panel

### ⚡ Accessibility Features (NEW)
- Touch emulation service for messaging apps
- Auto-detect and click send buttons
- User-controlled activation
- Supports WhatsApp, Telegram, Messenger, and more

## Technical Details

### Architecture
- **Language**: Kotlin
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 33 (Android 13)

### Key Libraries
- AndroidX Core KTX
- Material Components
- Security Crypto for encrypted storage
- Glide for image loading
- Coroutines for async operations

## Getting the APK for Testing

### 📥 Download Pre-built APK (Easiest Method)

The easiest way to get the APK is to download it from GitHub Actions:

1. Go to the [Actions](../../actions) tab
2. Click on the latest **"Build Android APK"** workflow run
3. Scroll down to **Artifacts** and download `app-debug`
4. Extract the ZIP file to get `app-debug.apk`

**Or manually trigger a build:**
1. Go to [Actions](../../actions) → "Build Android APK"
2. Click "Run workflow" → Select branch → "Run workflow"
3. Wait for completion and download the artifact

See [.github/workflows/README.md](.github/workflows/README.md) for detailed instructions.

## Building the App

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK with API 33

### Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/NekoClub/Repoone.git
cd Repoone
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build the APK:
```bash
./gradlew assembleDebug
```

The APK will be generated in `app/build/outputs/apk/debug/app-debug.apk`

### Release Build

To build a release APK:
```bash
./gradlew assembleRelease
```

## Installation

1. Enable "Install from Unknown Sources" in your Android device settings
2. Transfer the APK to your device
3. Open the APK file and follow the installation prompts

## Usage

### First Launch
1. Open the app
2. Set a 4-6 digit PIN for vault security
3. Confirm your PIN

### Adding Images
1. Tap the **+** button
2. Choose "Gallery" or "Camera"
3. Select or capture an image
4. Image is securely stored in your vault

### Editing Images
1. Long-press an image in the vault
2. Select "Edit Image"
3. Use the editing tools:
   - Rotate button for orientation
   - Filter button to choose adjustment type
   - Slider to adjust brightness, contrast, or saturation
4. Tap "Save" to apply changes

### Sharing Images
1. Long-press an image
2. Select "Share Image"
3. Choose your preferred social media or sharing app

### Using Overlay (NEW)
1. Grant overlay permission when prompted
2. Floating button appears on screen
3. Drag to position anywhere
4. Tap to expand for quick actions
5. Tap "Open App" for instant vault access

### Enabling Auto-Click (NEW)
1. Go to Settings
2. Tap "Enable Accessibility"
3. Enable the service in Android Settings
4. Return to app and toggle "Enable Auto-Click"
5. Auto-click now works in messaging apps

### Sharing to Vault (NEW)
1. View an image in any app (Gallery, Browser, etc.)
2. Tap Share
3. Select "Repoone Vault"
4. Enter PIN to authenticate
5. Image automatically imported to vault

### Changing PIN
1. Tap the settings icon (top right)
2. Select "Change PIN"
3. Enter current PIN, new PIN (must be 6+ digits with no simple patterns), and confirm
4. Note: New PIN must be different from old PIN and meet complexity requirements

## Security

### Hardline Security Features

**Authentication:**
- 6-digit minimum PIN with complexity validation
- No sequential numbers (123456, etc.)
- No repeated digits (111111, etc.)
- Cannot reuse previous PIN

**Progressive Lockout System:**
- 1st failed attempt: Warning
- 2nd failed attempt: 10-second lockout
- 3rd failed attempt: 30-second lockout
- 4th failed attempt: 2-minute lockout
- 5th failed attempt: **Automatic vault wipe (all images permanently deleted)**

**Forced PIN Expiry:**
- PINs expire after 30 days
- Warning reminders start at day 25
- Non-dismissible enforcement at day 30
- Aggressive nagging to ensure compliance

**Anti-Tampering:**
- Debugger detection with automatic exit
- Screenshot and screen recording prevention (FLAG_SECURE)
- No back button escape during authentication
- Access attempt logging with timestamps

**Data Protection:**
- PIN stored using AndroidX Security Crypto with AES256-GCM encryption
- Images stored in app-private storage
- File provider used for secure sharing
- No PIN recovery mechanism (forgotten PIN = permanent data loss)

### Security Philosophy

This app implements a **"security-first, convenience-last"** approach suitable for:
- High-security environments
- Sensitive document storage
- Enterprise/corporate deployments
- Users requiring maximum privacy protection

**⚠️ Important:** There is no PIN recovery option. If you forget your PIN or exceed 5 failed attempts, all vault contents are permanently deleted. This is by design for maximum security.

For detailed security feature documentation, see [EVIL_FEATURES.md](EVIL_FEATURES.md).

## Permissions

- **READ_EXTERNAL_STORAGE / READ_MEDIA_IMAGES**: Import images from gallery
- **CAMERA**: Take photos to add to vault
- **WRITE_EXTERNAL_STORAGE** (Android 9 and below): Save images
- **SYSTEM_ALERT_WINDOW** (NEW): Display overlay over other apps
- **BIND_ACCESSIBILITY_SERVICE** (NEW): Touch emulation for auto-clicking

## License

This project is open source and available under the MIT License.

## Documentation

For detailed information about the new features:
- **IMPLEMENTATION_SUMMARY.md** - Technical implementation details
- **ARCHITECTURE_DIAGRAM.md** - System architecture and flow diagrams
- **TESTING_GUIDE.md** - Comprehensive testing procedures
- **PR_SUMMARY.md** - Complete change summary

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.