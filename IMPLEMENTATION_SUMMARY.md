# Implementation Summary: Pervasive Permissions and Overlay Features

## Overview
This implementation adds advanced features to the Repoone secure vault app including:
- System-level overlay permissions
- Popup windows over other apps
- Auto intent assignment for receiving shared images
- Accessibility service for touch emulation (auto-clicking send buttons)

## Features Implemented

### 1. SYSTEM_ALERT_WINDOW Permission
**Location:** `AndroidManifest.xml`
- Added `<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />`
- This allows the app to display overlay windows on top of other apps

### 2. Overlay Service (OverlayService.kt)
**Purpose:** Displays a floating overlay button that persists across apps

**Features:**
- Draggable floating button
- Expandable panel with controls
- Quick access to the vault app
- Ability to close the overlay
- Automatically starts when overlay permission is granted

**How it works:**
- Creates a floating window using `WindowManager`
- Uses `TYPE_APPLICATION_OVERLAY` for Android O+ compatibility
- Provides touch listener for dragging functionality
- Expandable UI for additional controls

**Usage:**
- Overlay starts automatically when the MainActivity loads if permission is granted
- Users can drag the overlay anywhere on the screen
- Tap to expand/collapse options
- Options include: Open App, Close Overlay

### 3. Touch Emulation Service (TouchEmulationService.kt)
**Purpose:** Accessibility service that can automatically detect and click send buttons in messaging apps

**Features:**
- Monitors accessibility events in messaging apps
- Searches for buttons with "send", "share", or "post" text/labels
- Auto-clicks send buttons when detected
- Supports gesture-based clicking for complex UI elements
- Works with popular messaging apps: WhatsApp, Telegram, Instagram, Facebook Messenger, etc.

**How it works:**
- Implements `AccessibilityService` with gesture capabilities
- Listens to `TYPE_WINDOW_CONTENT_CHANGED` and `TYPE_WINDOW_STATE_CHANGED` events
- Recursively searches the UI tree for send buttons
- Performs click actions either via `ACTION_CLICK` or gesture dispatch

**Configuration:**
- Configuration file: `accessibility_service_config.xml`
- Targets specific package names (messaging apps)
- Requires manual enablement in Android Settings > Accessibility

### 4. Auto Intent Assignment (ShareReceiverActivity.kt)
**Purpose:** Automatically handles image sharing intents from other apps

**Features:**
- Receives images shared from any app
- Automatically imports shared images into the vault
- Handles authentication before import
- Supports direct SEND intents with image MIME type

**How it works:**
- Registered in `AndroidManifest.xml` with SEND intent filter
- Accepts `image/*` MIME type
- Extracts image URI from intent
- Routes through authentication before importing
- Imports image directly into vault after successful authentication

**Usage:**
- When users share an image from any app (Gallery, Camera, Browser, etc.)
- Select "Repoone Vault" as the share target
- Image is automatically imported after PIN authentication

### 5. Enhanced MainActivity
**New Features:**
- Permission check and request for SYSTEM_ALERT_WINDOW
- Automatic overlay service management
- Handler for shared images from other apps
- User prompt for overlay permission on first run

**Methods Added:**
- `checkAndRequestOverlayPermission()`: Checks and requests overlay permission
- `startOverlayService()`: Starts the overlay service
- `handleSharedImage()`: Processes images received via share intent

### 6. Enhanced Settings
**New Features:**
- Button to enable accessibility service
- Information dialog about accessibility service functionality
- Link to Android accessibility settings

**Methods Added:**
- `showAccessibilityInfo()`: Shows info and opens accessibility settings

### 7. UI Components

**Overlay Layout (overlay_layout.xml):**
- Floating button with drag handle
- Expand/collapse button
- Panel with Open App and Close Overlay buttons
- Semi-transparent background with rounded corners

**Overlay Background (overlay_background.xml):**
- Custom drawable with rounded corners
- Semi-transparent black background
- White border for visibility

## Permissions Required

### Manifest Permissions:
1. `SYSTEM_ALERT_WINDOW` - Display overlay windows
2. `BIND_ACCESSIBILITY_SERVICE` - Bind accessibility service (implicit)

### Runtime Permissions:
1. **Overlay Permission** - Requested via Settings intent
   - Required for API 23+
   - User must manually grant in system settings

2. **Accessibility Service** - Enabled in Settings
   - User navigates to Settings > Accessibility
   - Finds "Repoone Touch Emulation Service"
   - Manually enables the service

## User Flow

### First Time Setup:
1. User opens app and sets PIN
2. MainActivity prompts for overlay permission
3. User grants permission in system settings
4. Overlay appears on screen
5. User goes to Settings > Enable Accessibility
6. User enables accessibility service in system settings

### Using Auto Intent:
1. User views image in any app (Gallery, Chrome, etc.)
2. User taps Share button
3. User selects "Repoone Vault"
4. App opens to PIN entry (if locked)
5. After authentication, image is imported
6. User sees confirmation toast

### Using Overlay:
1. Floating button appears over all apps
2. User can drag it anywhere
3. Tap to expand options
4. Tap "Open App" to launch vault
5. Tap "Close Overlay" to hide it

### Using Touch Emulation:
1. User enables accessibility service
2. User opens a messaging app (WhatsApp, Telegram, etc.)
3. When send button appears, service auto-detects it
4. Service automatically clicks the send button
5. Message is sent without manual clicking

## Security Considerations

1. **Overlay Security:**
   - Overlay can be displayed over other apps
   - Could potentially be used for UI overlay attacks
   - Mitigated by requiring explicit user permission

2. **Accessibility Security:**
   - Service can read UI elements and perform gestures
   - Limited to specific messaging app packages
   - Requires explicit user enablement
   - User warned by Android about accessibility permissions

3. **Auto Intent Security:**
   - Only accepts image MIME types
   - Requires PIN authentication before import
   - Images are securely stored in app-private directory

## Testing Recommendations

### Manual Testing:
1. **Overlay:**
   - Grant overlay permission
   - Verify floating button appears
   - Test dragging functionality
   - Test expand/collapse
   - Test opening app from overlay
   - Test closing overlay

2. **Accessibility:**
   - Enable accessibility service
   - Open WhatsApp/Telegram
   - Compose a message
   - Verify send button is auto-clicked

3. **Share Intent:**
   - Open Gallery
   - Share an image
   - Select Repoone Vault
   - Verify authentication
   - Verify image import

### Edge Cases:
1. Permission denied scenarios
2. Service disabled/re-enabled
3. Multiple overlays
4. Screen rotation
5. App killed in background

## Compatibility

- **Minimum SDK:** 24 (Android 7.0)
- **Target SDK:** 33 (Android 13)
- **Overlay:** API 23+ (Android 6.0+)
- **Gestures:** API 24+ (Android 7.0+)
- **Foreground Service:** API 26+ (Android 8.0+)

## Known Limitations

1. **Overlay:**
   - User must manually grant permission
   - May be affected by battery optimization
   - Some devices restrict overlays

2. **Accessibility:**
   - User must manually enable
   - May not detect all button types
   - Package-specific implementation
   - Can be disabled by system

3. **Auto Intent:**
   - Only handles image MIME types
   - Requires authentication each time
   - Single image only (no batch)

## Future Enhancements

1. Add notification for overlay service
2. Implement foreground service for overlay
3. Add more messaging app packages
4. Improve button detection algorithms
5. Add batch image sharing
6. Add configurable auto-click delay
7. Add overlay customization options
8. Add accessibility service toggle in app settings

## Files Modified/Created

### New Files:
- `TouchEmulationService.kt` - Accessibility service implementation
- `OverlayService.kt` - Overlay service implementation
- `ShareReceiverActivity.kt` - Share intent receiver
- `overlay_layout.xml` - Overlay UI layout
- `overlay_background.xml` - Overlay background drawable
- `accessibility_service_config.xml` - Accessibility service configuration

### Modified Files:
- `AndroidManifest.xml` - Added permissions and service declarations
- `MainActivity.kt` - Added overlay management and share handling
- `AuthActivity.kt` - Added share intent pass-through
- `SettingsActivity.kt` - Added accessibility settings
- `activity_settings.xml` - Added accessibility button
- `strings.xml` - Added new string resources

## Code Quality Notes

1. **Error Handling:**
   - Proper null checks throughout
   - Try-catch blocks where needed
   - Graceful degradation

2. **Best Practices:**
   - Follows Android architecture patterns
   - Uses AndroidX libraries
   - Proper lifecycle management
   - Resource cleanup in onDestroy

3. **Documentation:**
   - Inline comments for complex logic
   - Method documentation
   - Clear variable naming

4. **Permissions:**
   - Proper permission checks
   - User-friendly permission requests
   - Fallback for denied permissions
