# Repoone Features Documentation

## Core Features

### 1. 🔒 Secure Vault System

The vault provides military-grade security for your private images:

- **Dual PIN Protection**: Separate Admin PIN and User PIN for control dynamics
- **Role-Based Access**: Administrator and Controlled User modes
- **Encrypted Storage**: Uses AndroidX Security Crypto with AES256-GCM encryption
- **Private Storage**: Images stored in app-private directory, not accessible to other apps
- **Auto-Lock**: App requires PIN on every launch
- **Access Logging**: Track all vault activities

#### Security Details:
- PINs are encrypted and stored using `EncryptedSharedPreferences`
- Master key uses AES256-GCM key scheme
- Images are stored in `/data/data/com.nekoclub.repoone/files/vault/`
- File provider ensures secure sharing without exposing file paths

### 2. 🎛️ Admin Control System (NEW)

**Techdomme/Kinkware Features** - Consensual control and restriction system:

#### Admin Settings
Access via Settings → Admin Settings (requires Admin PIN)

- **Admin PIN**: Separate PIN for administrator access to control settings
- **User Role Selection**: Switch between Administrator and Controlled User modes
- **Feature Permissions**: Enable/disable individual features for controlled users
  - Add Images
  - Delete Images
  - Edit Images
  - Share Images
  - Change User PIN
- **Access Logging**: View complete activity log with timestamps

#### Time-Based Access Restrictions
- **Scheduled Access**: Set specific hours when vault can be accessed
- **Configurable Time Windows**: Define start and end times
- **Automatic Enforcement**: App denies access outside allowed hours

#### Check-In System
- **Mandatory Check-Ins**: Require user to check in at regular intervals
- **Configurable Intervals**: Set check-in frequency (in hours)
- **Overdue Alerts**: User is prompted when check-in is overdue
- **Last Check-In Tracking**: View when last check-in occurred

#### How Admin Control Works:
1. First time: Set up Admin PIN in Settings
2. Configure user role (Admin or Controlled)
3. Enable desired restrictions
4. Set permissions for controlled user
5. All settings are locked behind Admin PIN
6. Controlled users cannot modify restrictions

### 3. 🎨 Image Editor

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

### 4. 📤 Social Media Sharing

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
- **Respects admin permissions**: Can be disabled for controlled users

### 5. 📸 Image Import

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
- **Note**: Can be restricted by admin permissions

### 6. ⚙️ Settings & Management

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

## Techdomme/Kinkware Usage

### For Administrators (Dom/Controller)

This app includes consensual control features designed for BDSM/kink dynamics:

#### Initial Setup:
1. Set up your regular User PIN first
2. Go to Settings → Admin Settings
3. Set an Admin PIN (keep this private!)
4. Configure user role to "Controlled User"
5. Enable desired restrictions

#### Available Controls:
- **Access Time Restrictions**: Limit when the vault can be accessed
- **Check-In Requirements**: Require regular check-ins to maintain access
- **Feature Permissions**: Control what the user can do:
  - Adding new images
  - Deleting images
  - Editing images
  - Sharing images
  - Changing their PIN
- **Activity Logging**: View complete log of all vault activities

#### Best Practices:
- Discuss and agree on all restrictions beforehand
- Start with lenient settings and adjust as needed
- Keep Admin PIN secure and separate from User PIN
- Regularly review activity logs
- Have a safeword/safe signal for emergencies
- Respect all boundaries and limits

### For Controlled Users (Sub)

#### Your Experience:
- Log in with your User PIN
- Features may be restricted based on admin settings
- You may be required to check in at intervals
- Access may be limited to certain times
- All activities are logged

#### Check-In Process:
1. Open the app during your allowed access time
2. If check-in is overdue, you'll see a prompt
3. Go to Settings → Check In
4. Complete the check-in
5. Continue using the app normally

#### Important Notes:
- You cannot change restrictions (admin-only)
- You cannot disable logging (admin-only)
- If PIN change is disabled, you'll see a grayed-out button
- Outside access hours, the app will immediately close
- Admin can view all your activities in the access log

### Safety & Consent

⚠️ **IMPORTANT SAFETY INFORMATION**:

- All control features are designed for **CONSENSUAL** use only
- Both parties must agree to all restrictions
- Have clear safe signals/safewords
- Regular check-ins about boundaries
- Admin should never abuse the access log or restrictions
- This is meant for kink/BDSM dynamics, not abuse
- If you feel unsafe, seek help immediately

### Technical Implementation

The control features are implemented with security in mind:

- Admin PIN stored separately with encryption
- Settings cannot be modified without Admin PIN
- Access logs are tamper-evident
- Time restrictions are enforced at app launch
- Feature permissions checked before each action
- All sensitive data encrypted with AES256-GCM

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
