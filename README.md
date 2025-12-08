# Repoone - Secure Vault & Image Editor

An Android application that provides secure image storage with PIN protection, built-in image editing capabilities, and seamless social media sharing.

## Features

### 🔒 Secure Vault
- PIN-protected image storage
- Encrypted preferences using Android Security Crypto
- Private storage for sensitive images

### 🎨 Image Editor
- **Rotate**: Rotate images 90 degrees
- **Filters**: Apply brightness, contrast, and saturation adjustments
- **Real-time preview**: See changes as you adjust

### 📤 Social Media Sharing
- Share images directly to social media platforms
- Supports any app that accepts image sharing via Android's share intent
- Includes: Facebook, Instagram, Twitter, WhatsApp, and more

### 📸 Import Options
- Import from gallery
- Take photos with camera
- Automatic secure storage

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

### Changing PIN
1. Tap the settings icon (top right)
2. Select "Change PIN"
3. Enter current PIN, new PIN, and confirm

## Security

- PIN is stored using AndroidX Security Crypto with AES256-GCM encryption
- Images are stored in app-private storage
- File provider is used for secure sharing

## Permissions

- **READ_EXTERNAL_STORAGE / READ_MEDIA_IMAGES**: Import images from gallery
- **CAMERA**: Take photos to add to vault
- **WRITE_EXTERNAL_STORAGE** (Android 9 and below): Save images

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.