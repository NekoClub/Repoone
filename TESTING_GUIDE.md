# Testing Guide for Pervasive Permissions Features

## Prerequisites
- Android device or emulator running Android 7.0 (API 24) or higher
- Android 8.0+ recommended for best overlay support

## Feature Testing Checklist

### 1. Overlay Permission and Service

#### Test 1.1: Overlay Permission Request
**Steps:**
1. Install and launch the app for the first time
2. Complete PIN setup
3. Observe permission dialog for overlay

**Expected Result:**
- Dialog appears with title "Overlay Permission"
- Message explains the need for overlay permission
- "Grant" and "Cancel" buttons are present

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 1.2: Granting Overlay Permission
**Steps:**
1. Click "Grant" in the permission dialog
2. Android Settings opens to overlay permission screen
3. Enable permission for Repoone Vault
4. Return to app

**Expected Result:**
- Settings screen opens correctly
- App-specific permission toggle is visible
- Returning to app shows floating overlay button

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 1.3: Overlay Button Display
**Steps:**
1. With overlay permission granted, observe the screen
2. Open another app (e.g., Chrome, Messages)
3. Verify overlay remains visible

**Expected Result:**
- Floating button appears in top-left area
- Button is visible over other apps
- Button has a semi-transparent background

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 1.4: Dragging Overlay
**Steps:**
1. Long-press and drag the overlay button
2. Move it to different screen positions
3. Release and verify it stays in position

**Expected Result:**
- Button follows finger movement smoothly
- Button can be positioned anywhere on screen
- Button remains at last position

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 1.5: Expanding Overlay
**Steps:**
1. Tap the expand arrow on the overlay
2. Observe the expanded panel

**Expected Result:**
- Panel expands showing "Open App" and "Close Overlay" buttons
- Buttons are clearly visible and clickable
- Panel has proper styling

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 1.6: Open App from Overlay
**Steps:**
1. Expand the overlay panel
2. Tap "Open App" button
3. Observe app behavior

**Expected Result:**
- App opens (or comes to foreground)
- If locked, authentication screen appears
- After auth, main vault screen shows

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 1.7: Close Overlay
**Steps:**
1. Expand the overlay panel
2. Tap "Close Overlay" button
3. Verify overlay disappears

**Expected Result:**
- Overlay immediately disappears
- Overlay service is stopped
- No residual UI elements remain

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

### 2. Auto Intent Assignment (Share Receiver)

#### Test 2.1: Share from Gallery
**Steps:**
1. Open device Gallery app
2. Select an image
3. Tap Share button
4. Look for "Repoone Vault" in share sheet

**Expected Result:**
- "Repoone Vault" appears as a share target
- Icon and name are clearly visible

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 2.2: Share Image Flow
**Steps:**
1. Share an image to Repoone Vault
2. Observe authentication screen
3. Enter correct PIN
4. Observe image import

**Expected Result:**
- App opens to PIN entry screen
- After correct PIN, image is imported
- Toast shows "Image imported from share"
- Image appears in vault

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 2.3: Share with Wrong PIN
**Steps:**
1. Share an image to Repoone Vault
2. Enter incorrect PIN
3. Observe behavior

**Expected Result:**
- "Wrong PIN" message appears
- Image is NOT imported
- Can retry with correct PIN

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 2.4: Share from Browser
**Steps:**
1. Open Chrome browser
2. Long-press an image
3. Select "Share image"
4. Choose "Repoone Vault"

**Expected Result:**
- Same flow as gallery share
- Image successfully imported after auth

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

### 3. Accessibility Service (Touch Emulation)

#### Test 3.1: Enable Accessibility Service
**Steps:**
1. Open Repoone Vault app
2. Go to Settings
3. Tap "Enable Accessibility" button
4. Observe information dialog

**Expected Result:**
- Dialog appears with service explanation
- "Open Settings" button is present
- Clicking it opens Android Accessibility Settings

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 3.2: Activate Service in Settings
**Steps:**
1. In Accessibility Settings, find "Repoone Touch Emulation Service"
2. Tap to open service settings
3. Enable the service
4. Accept the permission warning

**Expected Result:**
- Service appears in accessibility list
- Detailed service info shown
- Warning about accessibility permissions displayed
- Service successfully enabled

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 3.3: Enable Auto-Click Toggle
**Steps:**
1. Return to Repoone Vault Settings
2. Find "Enable Auto-Click" switch
3. Toggle it ON
4. Observe feedback

**Expected Result:**
- Switch toggles to ON state
- Toast shows "Auto-click enabled"
- Setting is persisted

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 3.4: Auto-Click in WhatsApp
**Steps:**
1. Open WhatsApp
2. Open a chat
3. Type a message (don't send)
4. Wait and observe

**Expected Result:**
- After 1-second delay, send button is auto-clicked
- Message is sent automatically
- Only one message sent (debouncing works)

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 3.5: Auto-Click in Telegram
**Steps:**
1. Open Telegram
2. Open a chat
3. Type a message
4. Observe auto-send behavior

**Expected Result:**
- Similar to WhatsApp test
- Send button auto-clicked after delay
- Message sent successfully

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 3.6: Disable Auto-Click
**Steps:**
1. Return to Repoone Settings
2. Toggle "Enable Auto-Click" switch OFF
3. Test messaging app again

**Expected Result:**
- Switch toggles to OFF state
- Toast shows "Auto-click disabled"
- No auto-clicking occurs in messaging apps

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 3.7: Debouncing Test
**Steps:**
1. Enable auto-click
2. Open messaging app
3. Type multiple messages quickly
4. Observe click behavior

**Expected Result:**
- Only one message sent per second
- No rapid-fire sending
- Debouncing prevents multiple clicks

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

### 4. Integration Tests

#### Test 4.1: Screen Rotation
**Steps:**
1. With overlay visible, rotate device
2. Observe overlay behavior

**Expected Result:**
- Overlay remains visible after rotation
- Position may adjust but overlay persists
- No crashes or UI glitches

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 4.2: Background/Foreground
**Steps:**
1. Enable all features
2. Press Home button
3. Wait a few seconds
4. Return to app

**Expected Result:**
- Overlay remains visible
- Services continue running
- App state is preserved

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 4.3: Reboot Persistence
**Steps:**
1. Enable all features
2. Reboot device
3. Check feature states

**Expected Result:**
- Overlay permission persists
- Accessibility service remains enabled
- Auto-click toggle state preserved
- May need to restart overlay service

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 4.4: Battery Optimization
**Steps:**
1. Check battery optimization settings
2. Test with optimization enabled/disabled

**Expected Result:**
- Features work with optimization on
- Better reliability with optimization off
- No crashes in either case

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

### 5. Edge Cases and Error Handling

#### Test 5.1: Permission Denied
**Steps:**
1. Deny overlay permission
2. Observe app behavior

**Expected Result:**
- App continues to function normally
- No crashes
- Overlay features simply unavailable

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 5.2: Service Disabled
**Steps:**
1. Disable accessibility service in settings
2. Keep auto-click toggle ON in app
3. Test messaging apps

**Expected Result:**
- No auto-clicking occurs
- App doesn't crash
- Service gracefully handles disabled state

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 5.3: Multiple Overlays
**Steps:**
1. Enable overlay in Repoone
2. Enable overlay in another app (if available)
3. Test both

**Expected Result:**
- Both overlays visible
- No conflicts or crashes
- Each overlay functions independently

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 5.4: Low Memory
**Steps:**
1. Open many apps
2. Fill device memory
3. Test all features

**Expected Result:**
- Overlay may be killed to save memory
- Service automatically restarts when possible
- No data loss or corruption

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

### 6. Security Tests

#### Test 6.1: Auto-Click Default State
**Steps:**
1. Fresh install
2. Enable accessibility service
3. Check auto-click state

**Expected Result:**
- Auto-click toggle is OFF by default
- No auto-clicking until explicitly enabled
- User must consciously enable feature

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 6.2: Share Authentication
**Steps:**
1. Share image from another app
2. Attempt to bypass PIN entry

**Expected Result:**
- Cannot bypass authentication
- Image only imported after correct PIN
- Secure vault access maintained

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

#### Test 6.3: Overlay Security
**Steps:**
1. Try to interact with secure screens
2. Observe overlay behavior

**Expected Result:**
- Overlay cannot capture sensitive input
- Android system protects secure screens
- No security vulnerabilities

**Actual Result:** [ ]
**Status:** [ ] Pass [ ] Fail

## Performance Metrics

Record the following metrics during testing:

1. **App Launch Time:**
   - Cold start: _____ ms
   - Warm start: _____ ms

2. **Overlay Performance:**
   - Drag responsiveness: [ ] Excellent [ ] Good [ ] Poor
   - Memory usage: _____ MB
   - CPU usage: _____ %

3. **Accessibility Service:**
   - Button detection time: _____ ms
   - False positive rate: _____ %
   - Memory footprint: _____ MB

4. **Battery Impact:**
   - Battery drain per hour: _____ %
   - Acceptable? [ ] Yes [ ] No

## Known Issues

Document any issues found during testing:

1. Issue: ________________________________
   Severity: [ ] Critical [ ] Major [ ] Minor
   Steps to Reproduce: ____________________
   
2. Issue: ________________________________
   Severity: [ ] Critical [ ] Major [ ] Minor
   Steps to Reproduce: ____________________

## Test Summary

- Total Tests: ___
- Passed: ___
- Failed: ___
- Skipped: ___
- Pass Rate: ___%

## Tester Information

- Tester Name: ________________
- Test Date: __________________
- Device Model: _______________
- Android Version: ____________
- App Version: ________________

## Additional Notes

_______________________________________
_______________________________________
_______________________________________
