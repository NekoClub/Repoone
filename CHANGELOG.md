# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-12-08

### Added
- Initial release of Repoone - Secure Vault & Image Editor
- PIN-based authentication system with encrypted storage
- Secure vault for storing private images
- Image import from gallery
- Camera integration for capturing photos directly
- Image editor with the following features:
  - Rotate images (90-degree increments)
  - Brightness adjustment
  - Contrast adjustment
  - Saturation adjustment
- Social media sharing integration
- Settings page with PIN change functionality
- Material Design UI with grid layout
- FileProvider for secure file sharing
- Launcher icons for all density screens
- Comprehensive documentation (README, BUILD_INSTRUCTIONS, FEATURES)

### Security
- AES256-GCM encryption for PIN storage
- Private app storage for images
- Secure file sharing via FileProvider
- No internet connectivity required
- No data collection or analytics

### Supported Platforms
- Android 7.0 (API 24) and higher
- Target SDK: Android 13 (API 33)

### Known Limitations
- Crop tool shows notification but not fully implemented
- No video support
- No biometric authentication
- Single vault folder only
- No cloud backup

## [Unreleased]

### Planned Features
- Biometric authentication (fingerprint/face unlock)
- Advanced crop tool with custom aspect ratios
- More filter presets (grayscale, sepia, vintage)
- Video storage and playback
- Multiple vault folders with custom names
- Image tagging and search functionality
- Bulk operations (multi-select, batch delete)
- Slideshow mode
- Encrypted cloud backup (optional)
- Dark mode theme
- Import/export vault data
- File size optimization options
- EXIF data viewer and editor

### Planned Improvements
- Performance optimization for large image libraries
- Better image compression options
- Undo/redo functionality in editor
- Image history/versioning
- Gesture controls in editor
- Tutorial/onboarding for new users

---

## Version History

- **1.0.0** (2024-12-08) - Initial Release
