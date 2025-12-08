# Repoone UI Screenshots & Design

## App Screens Description

Since this is a newly created app without an actual build, here are detailed descriptions of what each screen looks like and how they function.

---

## 1. Launcher / Splash Screen

**Screen**: `LauncherActivity`

**Description**:
- Brief splash screen (logo or app name)
- Automatically checks for existing PIN
- Redirects to appropriate screen

**Visual Elements**:
- App logo/icon centered
- App name "Repoone Vault"
- Material theme purple background

**Flow**: Appears for <1 second, then navigates automatically

---

## 2. PIN Setup Screen (First Launch)

**Screen**: `AuthActivity` (Setup Mode)

**Description**:
When app is launched for the first time, users must set up a PIN.

**Visual Elements**:
```
┌──────────────────────────────────┐
│         Set PIN                  │
│                                  │
│  ┌────────────────────────────┐  │
│  │ Enter PIN: ████            │  │
│  └────────────────────────────┘  │
│                                  │
│  [ Set PIN ]                     │
│                                  │
└──────────────────────────────────┘
```

**Features**:
- Large title "Set PIN"
- Text input field (numeric, password type)
- Shows dots instead of numbers
- "Set PIN" button
- Material Design styling

**Interaction**:
1. User enters 4-6 digit PIN
2. Taps "Set PIN"
3. Screen updates to "Confirm PIN"
4. User re-enters same PIN
5. If match → Vault opens
6. If mismatch → Reset and try again

---

## 3. PIN Unlock Screen

**Screen**: `AuthActivity` (Unlock Mode)

**Description**:
Every time app is opened after initial setup, this screen appears.

**Visual Elements**:
```
┌──────────────────────────────────┐
│       Unlock Vault               │
│                                  │
│  ┌────────────────────────────┐  │
│  │ Enter PIN: ████            │  │
│  └────────────────────────────┘  │
│                                  │
│  [ Unlock Vault ]                │
│                                  │
└──────────────────────────────────┘
```

**Features**:
- Title "Unlock Vault"
- PIN input field
- "Unlock Vault" button
- Material purple theme

**Interaction**:
1. User enters PIN
2. Taps "Unlock Vault"
3. If correct → Opens MainActivity
4. If wrong → Shows error, field clears

---

## 4. Main Vault Screen (Empty State)

**Screen**: `MainActivity` (No Images)

**Description**:
The main screen when vault is empty.

**Visual Elements**:
```
┌──────────────────────────────────┐
│  My Vault              [⚙️]      │
├──────────────────────────────────┤
│                                  │
│                                  │
│         No images                │
│                                  │
│     Tap + to add your            │
│         first image              │
│                                  │
│                                  │
│                            [+]   │
└──────────────────────────────────┘
```

**Features**:
- App title "My Vault" in toolbar
- Settings icon (gear) in top right
- Empty state message centered
- Purple FAB (Floating Action Button) with + icon
- Material Design elevation and shadows

**Interaction**:
- Tap + button → Opens image source dialog
- Tap settings → Opens SettingsActivity

---

## 5. Main Vault Screen (With Images)

**Screen**: `MainActivity` (Grid View)

**Description**:
Main screen showing user's vault images in a grid.

**Visual Elements**:
```
┌──────────────────────────────────┐
│  My Vault              [⚙️]      │
├──────────────────────────────────┤
│  ┌───────┐ ┌───────┐             │
│  │ IMG 1 │ │ IMG 2 │             │
│  │       │ │       │             │
│  └───────┘ └───────┘             │
│  ┌───────┐ ┌───────┐             │
│  │ IMG 3 │ │ IMG 4 │             │
│  │       │ │       │             │
│  └───────┘ └───────┘             │
│  ┌───────┐ ┌───────┐             │
│  │ IMG 5 │ │ IMG 6 │             │
│  │       │ │       │             │
│  └───────┘ └───────┘             │
│                            [+]   │
└──────────────────────────────────┘
```

**Features**:
- 2-column grid layout
- Card-style image thumbnails
- Elevation and rounded corners (8dp)
- Images fill card area (centerCrop)
- Smooth scrolling
- FAB always visible at bottom right

**Interaction**:
- Tap image → Opens ImageEditorActivity
- Long-press image → Shows context menu
  - Edit Image
  - Share Image
  - Delete Image
- Tap + → Add new image

---

## 6. Add Image Dialog

**Description**:
Dialog that appears when FAB is tapped.

**Visual Elements**:
```
┌──────────────────────────┐
│    Add Image             │
├──────────────────────────┤
│                          │
│  📱  Gallery             │
│                          │
│  📷  Camera              │
│                          │
└──────────────────────────┘
```

**Features**:
- Simple list dialog
- Two options with icons
- Material Design

**Interaction**:
- Tap "Gallery" → Opens system image picker
- Tap "Camera" → Opens camera intent
- Tap outside → Closes dialog

---

## 7. Image Editor Screen

**Screen**: `ImageEditorActivity`

**Description**:
Full-featured image editor with controls.

**Visual Elements**:
```
┌──────────────────────────────────┐
│  ← Edit Image                    │
├──────────────────────────────────┤
│                                  │
│                                  │
│         [IMAGE PREVIEW]          │
│          (Full size)             │
│                                  │
│                                  │
├──────────────────────────────────┤
│  [Rotate] [Crop] [Filter]        │
│                                  │
│  Brightness:                     │
│  ├─────────o──────┤              │
│                                  │
│  [ Save ]    [ Cancel ]          │
└──────────────────────────────────┘
```

**Features**:
- Back button in toolbar
- Large image preview (centerInside)
- Three action buttons (Rotate, Crop, Filter)
- Adjustment label and slider
- Save and Cancel buttons
- Material buttons with proper spacing

**Interaction**:
1. **Rotate**: Tap to rotate 90° clockwise, updates immediately
2. **Crop**: Shows notification (not fully implemented)
3. **Filter**: Opens filter selection dialog
4. **Slider**: Real-time adjustment preview
5. **Save**: Writes changes to vault, returns to MainActivity
6. **Cancel**: Discards changes, returns to MainActivity

---

## 8. Filter Selection Dialog

**Description**:
Dialog for choosing filter type in editor.

**Visual Elements**:
```
┌──────────────────────────┐
│    Filter                │
├──────────────────────────┤
│                          │
│  Brightness              │
│                          │
│  Contrast                │
│                          │
│  Saturation              │
│                          │
└──────────────────────────┘
```

**Features**:
- Simple list of filter types
- Material Design list

**Interaction**:
- Tap filter type → Updates label and resets slider
- Changes take effect via slider

---

## 9. Image Long-Press Menu

**Description**:
Context menu when long-pressing an image.

**Visual Elements**:
```
┌──────────────────────────┐
│                          │
│  Edit Image              │
│                          │
│  Share Image             │
│                          │
│  Delete Image            │
│                          │
└──────────────────────────┘
```

**Features**:
- Simple action list
- Material Design

**Interaction**:
- Edit → Opens ImageEditorActivity
- Share → Opens Android share sheet
- Delete → Shows confirmation dialog

---

## 10. Delete Confirmation Dialog

**Description**:
Confirmation before deleting an image.

**Visual Elements**:
```
┌──────────────────────────────┐
│  Are you sure you want to    │
│  delete this image?          │
│                              │
│              [ No ]  [ Yes ] │
└──────────────────────────────┘
```

**Features**:
- Simple yes/no dialog
- Material Design

**Interaction**:
- Yes → Deletes image, refreshes grid
- No → Closes dialog

---

## 11. Share Sheet

**Description**:
System share dialog for sharing images.

**Visual Elements**:
```
┌──────────────────────────────────┐
│  Share to...                     │
├──────────────────────────────────┤
│  [📱] Messages    [📘] Facebook  │
│  [📷] Instagram   [🐦] Twitter   │
│  [💬] WhatsApp    [✉️] Email     │
│  [📋] Copy        [💾] Save      │
└──────────────────────────────────┘
```

**Features**:
- System-provided share sheet
- Shows all apps that accept images
- Icons from each app

**Interaction**:
- Tap app → Shares image to that app
- Image shared via secure content URI

---

## 12. Settings Screen

**Screen**: `SettingsActivity`

**Description**:
Simple settings screen for app configuration.

**Visual Elements**:
```
┌──────────────────────────────────┐
│  ← Settings                      │
├──────────────────────────────────┤
│                                  │
│  [ Change PIN ]                  │
│                                  │
│  About                           │
│  ──────                          │
│  Repoone Vault                   │
│  Version 1.0                     │
│                                  │
└──────────────────────────────────┘
```

**Features**:
- Back button in toolbar
- "Change PIN" button
- About section with app info
- Simple, clean layout

**Interaction**:
- Back → Returns to MainActivity
- Change PIN → Opens change PIN dialog

---

## 13. Change PIN Dialog

**Description**:
Dialog for changing vault PIN.

**Visual Elements**:
```
┌──────────────────────────────────┐
│  Change PIN                      │
├──────────────────────────────────┤
│  Current PIN:                    │
│  ┌────────────────────────────┐  │
│  │ ████                       │  │
│  └────────────────────────────┘  │
│                                  │
│  New PIN:                        │
│  ┌────────────────────────────┐  │
│  │ ████                       │  │
│  └────────────────────────────┘  │
│                                  │
│  Confirm New PIN:                │
│  ┌────────────────────────────┐  │
│  │ ████                       │  │
│  └────────────────────────────┘  │
│                                  │
│         [ Cancel ]  [ Save ]     │
└──────────────────────────────────┘
```

**Features**:
- Three password input fields
- Material Design text inputs
- Save and Cancel buttons

**Interaction**:
1. Enter current PIN
2. Enter new PIN
3. Confirm new PIN
4. Tap Save → Validates and saves
5. Tap Cancel → Closes dialog

---

## Design System

### Colors
- **Primary**: Purple 500 (#6200EE)
- **Primary Variant**: Purple 700 (#3700B3)
- **Secondary**: Teal 200 (#03DAC5)
- **Background**: White (#FFFFFF)
- **Surface**: White (#FFFFFF)
- **Error**: Red (Material default)

### Typography
- **Titles**: 24sp, Bold
- **Body**: 16sp, Regular
- **Buttons**: 14sp, Medium (All Caps)
- **Captions**: 12sp, Regular

### Spacing
- **Padding**: 16dp (standard), 24dp (large)
- **Margin**: 4dp (small), 8dp (medium), 16dp (large)
- **Elevation**: 4dp (cards), 6dp (FAB)
- **Corners**: 8dp (cards), 56dp (FAB)

### Icons
- **System Icons**: Android drawable resources
- **Size**: 24dp (standard)
- **Color**: White on primary, dark gray on surface

### Animations
- **Transitions**: Material standard (300ms)
- **FAB**: Scale and rotate on tap
- **Cards**: Ripple effect on tap
- **Dialogs**: Fade in/out

---

## Accessibility Features

- High contrast colors
- Large touch targets (48dp minimum)
- Content descriptions on all images
- Support for TalkBack
- Clear visual hierarchy
- Readable font sizes

---

## Responsive Design

### Portrait Mode
- Standard layout as shown above
- 2-column grid for images
- FAB in bottom right

### Landscape Mode (Adaptive)
- 3-4 column grid for images
- Same controls, optimized layout
- FAB remains bottom right

### Tablet Support
- Wider layouts utilized
- More columns in grid (3-4)
- Larger preview in editor
- All features scale appropriately

---

## UI/UX Principles Applied

1. **Material Design**: Google's design system throughout
2. **Consistency**: Same patterns across all screens
3. **Feedback**: Visual feedback for all actions
4. **Simplicity**: Clean, uncluttered interfaces
5. **Security**: No visual PIN hints or previews
6. **Efficiency**: Quick access to common actions
7. **Error Prevention**: Confirmation dialogs
8. **Recognition**: Icons and labels together

---

## Future UI Enhancements

- Dark theme support
- Custom color schemes
- Animated transitions
- Grid/List view toggle
- Zoom gestures in editor
- Crop overlay with guides
- Filter preview thumbnails
- Onboarding tutorial
- Advanced editor UI with history
