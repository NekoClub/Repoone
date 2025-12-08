# Contributing to Repoone

Thank you for your interest in contributing to Repoone! This document provides guidelines and instructions for contributing.

## How to Contribute

### Reporting Bugs

If you find a bug, please create an issue with:
- Clear description of the problem
- Steps to reproduce
- Expected behavior
- Actual behavior
- Device/Android version
- Screenshots if applicable

### Suggesting Features

Feature requests are welcome! Please create an issue with:
- Clear description of the feature
- Use case and motivation
- Proposed implementation (optional)
- Mockups or examples (optional)

### Pull Requests

1. **Fork the Repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Repoone.git
   cd Repoone
   ```

2. **Create a Branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/your-bug-fix
   ```

3. **Make Your Changes**
   - Follow the existing code style
   - Write clear, descriptive commit messages
   - Test your changes thoroughly
   - Update documentation as needed

4. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "Add feature: description of your change"
   ```

5. **Push to Your Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create Pull Request**
   - Go to the original repository
   - Click "New Pull Request"
   - Select your branch
   - Describe your changes
   - Link related issues

## Code Style Guidelines

### Kotlin

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused
- Use Kotlin idioms (let, apply, with, etc.)

### XML

- Use meaningful IDs (`btn_save` not `button1`)
- Follow Material Design guidelines
- Keep layouts simple and maintainable
- Use `dp` for dimensions, `sp` for text sizes
- Group related attributes together

### Example:
```kotlin
// Good
fun validatePin(pin: String): Boolean {
    return pin.length >= 4 && pin.all { it.isDigit() }
}

// Avoid
fun vp(p: String): Boolean {
    if (p.length >= 4) {
        for (c in p) {
            if (!c.isDigit()) return false
        }
        return true
    }
    return false
}
```

## Project Structure

```
Repoone/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/nekoclub/repoone/
│   │   │   │   ├── MainActivity.kt          # Main vault screen
│   │   │   │   ├── AuthActivity.kt          # PIN authentication
│   │   │   │   ├── ImageEditorActivity.kt   # Image editing
│   │   │   │   ├── SettingsActivity.kt      # App settings
│   │   │   │   ├── LauncherActivity.kt      # Entry point
│   │   │   │   ├── VaultManager.kt          # Image storage logic
│   │   │   │   ├── SecurePreferences.kt     # Encrypted storage
│   │   │   │   ├── ImageAdapter.kt          # RecyclerView adapter
│   │   │   │   └── VaultImage.kt            # Data model
│   │   │   ├── res/
│   │   │   │   ├── layout/                  # XML layouts
│   │   │   │   ├── values/                  # Strings, colors, themes
│   │   │   │   └── drawable/                # Icons and images
│   │   │   └── AndroidManifest.xml
│   │   └── test/                            # Unit tests (future)
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## Testing

### Manual Testing

Before submitting a PR, please test:
1. PIN setup and authentication
2. Image import (gallery and camera)
3. Image editing (rotate, filters)
4. Image sharing
5. Settings (change PIN)
6. Delete functionality

### Automated Testing (Future)

We plan to add:
- Unit tests for business logic
- UI tests with Espresso
- Integration tests

## Security Guidelines

This app handles sensitive user data. Please:
- Never log PIN or sensitive data
- Use encrypted storage for secrets
- Validate all user inputs
- Follow Android security best practices
- Report security issues privately

### Reporting Security Issues

**Do not** create public issues for security vulnerabilities.
Email: [Your security email]

## Feature Development Priority

### High Priority
- Biometric authentication (fingerprint/face)
- Crop tool implementation
- Bulk operations (multi-select)
- Video support

### Medium Priority
- Additional filters (grayscale, sepia)
- Multiple vault folders
- Image tagging/search
- Slideshow mode

### Low Priority
- Cloud backup (encrypted)
- Themes (dark/light)
- Advanced editing (curves, levels)

## Resources

- [Android Developers Guide](https://developer.android.com/guide)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Material Design](https://material.io/design)
- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)

## Code Review Process

1. **Submission**: Create PR with clear description
2. **Initial Review**: Maintainer reviews within 1-2 weeks
3. **Feedback**: Address any requested changes
4. **Approval**: PR approved by maintainer
5. **Merge**: Changes merged to main branch

## Questions?

Feel free to:
- Open an issue for questions
- Join discussions in existing issues
- Contact maintainers

Thank you for contributing to Repoone! 🎉
