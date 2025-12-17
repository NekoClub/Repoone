# GitHub Actions Workflows

## Build Android APK

This workflow automatically builds the Android APK for testing.

### When does it run?

- **On Push**: When code is pushed to `main` or `develop` branches
- **On Pull Request**: When a PR is created targeting `main` or `develop` branches
- **Manual Trigger**: Can be manually triggered from the Actions tab
- **On Release**: When a release is created or published

### How to get the APK for testing?

#### Method 1: Download from GitHub Actions (Recommended)

1. Go to the **Actions** tab in the GitHub repository
2. Click on the latest **"Build Android APK"** workflow run
3. Scroll down to the **Artifacts** section at the bottom
4. Download either:
   - `app-debug` - Debug APK for testing (recommended for development)
   - `app-release` - Release APK (unsigned)

#### Method 2: Manual Trigger

1. Go to the **Actions** tab in the GitHub repository
2. Click on **"Build Android APK"** in the left sidebar
3. Click the **"Run workflow"** button on the right
4. Select the branch you want to build from
5. Click **"Run workflow"**
6. Wait for the workflow to complete
7. Download the APK from the Artifacts section

#### Method 3: From Releases (For stable versions)

1. Go to the **Releases** section in the GitHub repository
2. Find the release you want to download
3. Download the APK file attached to the release

### APK Details

- **Debug APK**: `app-debug.apk`
  - Suitable for development and testing
  - Includes debugging information
  - Larger file size
  - Can be installed alongside other versions

- **Release APK**: `app-release-unsigned.apk`
  - Optimized for production
  - Smaller file size
  - Not signed (you'll need to sign it for distribution)

### Installing the APK

1. Download the APK file to your Android device
2. Enable "Install from Unknown Sources" in your device settings
3. Open the APK file and tap "Install"
4. Grant necessary permissions when prompted

### Artifact Retention

- Artifacts are kept for **30 days** after the workflow run
- Download them before they expire if you need them for reference

### Troubleshooting

**Workflow Failed?**
- Check the workflow logs in the Actions tab for error messages
- Common issues:
  - Gradle build errors (check build.gradle files)
  - Missing dependencies
  - SDK version mismatches

**Can't download artifacts?**
- Make sure you're logged into GitHub
- Artifacts are only available to repository members
- Check if the workflow completed successfully

**APK won't install?**
- Ensure "Install from Unknown Sources" is enabled
- Check device Android version (minimum: Android 7.0 / API 24)
- Try uninstalling previous versions first
