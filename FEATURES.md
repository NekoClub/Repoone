# Repoone Features Documentation

## Core Features

### 1. 🔒 Secure Vault System

The vault provides military-grade security for your private images:

- **PIN Protection**: Set a 4-6 digit PIN to secure your vault
- **Encrypted Storage**: Uses AndroidX Security Crypto with AES256-GCM encryption
- **Private Storage**: Images stored in app-private directory, not accessible to other apps
- **Auto-Lock**: App requires PIN on every launch

#### Security Details:
- PIN is encrypted and stored using `EncryptedSharedPreferences`
- Master key uses AES256-GCM key scheme
- Images are stored in `/data/data/com.nekoclub.repoone/files/vault/`
- File provider ensures secure sharing without exposing file paths

### 2. 🎨 Image Editor

Built-in editor with multiple adjustment capabilities:

#### Rotate
- Rotate images 90 degrees clockwise
- Multiple rotations supported (90°, 180°, 270°)
- Maintains image quality

#### Filters & Adjustments
- **Brightness**: Adjust from 0-200% (slider control)
- **Contrast**: Fine-tune image contrast
- **Saturation**: Control color intensity
- Real-time preview of all adjustments
- Non-destructive editing until saved

#### How to Use:
1. Long-press any image in the vault
2. Select "Edit Image"
3. Choose adjustment type using "Filter" button
4. Use slider to adjust the selected property
5. Tap "Rotate" to change orientation
6. Press "Save" to apply changes permanently

### 3. 📤 Social Media Sharing

Share your vault images to any platform:

#### Supported Platforms:
- Facebook
- Instagram
- Twitter / X
- WhatsApp
- Telegram
- Snapchat
- Email
- SMS/MMS
- Any app that accepts image sharing

#### How It Works:
1. Long-press an image
2. Select "Share Image"
3. Choose your preferred app from the share sheet
4. Image is securely shared via Android's FileProvider
5. Original remains safely in your vault

#### Privacy Features:
- Shared images use temporary URIs
- No permanent copies created outside vault
- File paths not exposed to receiving apps
- Permission automatically revoked after sharing

### 4. 📸 Image Import

Multiple ways to add images to your vault:

#### Gallery Import
- Browse and select images from device gallery
- Supports single image selection
- Original file remains in gallery (copy made to vault)

#### Camera Capture
- Take photos directly within the app
- Photos immediately stored in vault
- Requires camera permission
- Works with front and rear cameras

#### Process:
1. Tap the **+** button in the main screen
2. Choose "Gallery" or "Camera"
3. Select/capture your image
4. Image automatically encrypted and stored
5. Appears instantly in your vault

### 5. ⚙️ Settings & Management

#### Change PIN
- Update your vault PIN anytime
- Requires current PIN for verification
- Confirm new PIN to prevent typos

#### Image Management
- Grid view of all vault images
- Sort by date added (newest first)
- Long-press for context menu:
  - Edit Image
  - Share Image
  - Delete Image

#### Delete Images
- Confirmation dialog prevents accidents
- Permanent deletion (no recovery)
- Storage space immediately freed

## User Interface

### Main Screen
- Clean grid layout (2 columns)
- Thumbnail previews of all images
- Floating Action Button (FAB) to add images
- Settings icon in toolbar
- Empty state message when vault is empty

### Authentication Screen
- Simple, focused design
- Clear PIN input field
- Numeric keyboard for PIN entry
- First-time setup wizard for new users

### Image Editor Screen
- Full-screen image preview
- Bottom toolbar with controls
- Real-time adjustment feedback
- Easy save/cancel options

## Technical Specifications

### Supported Image Formats
- JPEG (.jpg, .jpeg)
- PNG (converted to JPEG in vault)
- Any format supported by Android's BitmapFactory

### Image Storage
- Format: JPEG (quality: 95%)
- Location: App-private external files directory
- Naming: UUID-based filenames for uniqueness
- Metadata: Timestamp of addition

### Permissions Required
- **READ_MEDIA_IMAGES** (Android 13+): Gallery access
- **READ_EXTERNAL_STORAGE** (Android 12 and below): Gallery access
- **CAMERA**: Take photos
- **WRITE_EXTERNAL_STORAGE** (Android 9 and below): Save images

### Minimum Requirements
- Android 7.0 (API 24) or higher
- 50 MB free storage space
- Camera (optional, for camera feature)

## Privacy & Security

### Data Protection
- No internet connectivity required
- No analytics or tracking
- No ads
- No data collection
- All data stored locally

### Best Practices
1. Use a strong, unique PIN
2. Don't share your PIN
3. Regularly backup important images (via export)
4. Clear app cache if device is being sold/given away

## Future Feature Considerations

Potential enhancements for future versions:
- Crop tool with custom aspect ratios
- More filter presets (Grayscale, Sepia, etc.)
- Video support
- Biometric authentication (fingerprint/face)
- Cloud backup (encrypted)
- Multiple vault folders
- Image search/tagging
- Slideshow mode
- Bulk operations (multi-select)

## Troubleshooting

### Common Issues

**App crashes on startup**
- Clear app data and restart
- Reinstall the app
- Check Android version compatibility

**Can't import images**
- Grant storage permissions in Settings
- Check if images are corrupted
- Ensure sufficient storage space

**PIN forgotten**
- Uninstall and reinstall app (loses all vault data)
- No recovery method available (by design for security)

**Images not showing**
- Check if vault directory exists
- Restart the app
- Reimport images

**Share not working**
- Ensure target app is installed
- Grant necessary permissions
- Try different share target

## Support

For issues, feature requests, or contributions:
- GitHub Issues: https://github.com/NekoClub/Repoone/issues
- Pull Requests: https://github.com/NekoClub/Repoone/pulls
