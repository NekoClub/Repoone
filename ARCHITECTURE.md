# Repoone Architecture

## Application Flow

```
┌─────────────────┐
│ LauncherActivity│  (Entry Point)
└────────┬────────┘
         │
         ├─── Check if PIN exists
         │
         ├─── No PIN ────────┐
         │                   ↓
         │         ┌──────────────────┐
         │         │  AuthActivity    │ (Setup Mode)
         │         │  - Set new PIN   │
         │         │  - Confirm PIN   │
         │         └────────┬─────────┘
         │                  │
         ├─── Has PIN ──────┤
         │                  ↓
         │         ┌──────────────────┐
         │         │  AuthActivity    │ (Unlock Mode)
         │         │  - Enter PIN     │
         │         │  - Validate      │
         │         └────────┬─────────┘
         │                  │
         └──────────────────┤
                           ↓
                  ┌─────────────────┐
                  │  MainActivity   │ (Vault Screen)
                  │  - Grid of images
                  │  - Add button   │
                  │  - Settings     │
                  └────┬─────┬──────┘
                       │     │
         ┌─────────────┘     └─────────────┐
         │                                  │
         ↓                                  ↓
┌────────────────────┐          ┌──────────────────┐
│ImageEditorActivity │          │ SettingsActivity │
│  - Rotate          │          │  - Change PIN    │
│  - Filters         │          └──────────────────┘
│  - Adjustments     │
│  - Save/Cancel     │
└────────────────────┘
```

## Component Architecture

### Data Layer

```
┌─────────────────────────────────────────────────┐
│                 Data Models                      │
├─────────────────────────────────────────────────┤
│ VaultImage                                      │
│  - id: String                                   │
│  - file: File                                   │
│  - timestamp: Long                              │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│              Storage Managers                    │
├─────────────────────────────────────────────────┤
│ VaultManager                                    │
│  - importImage(InputStream): VaultImage         │
│  - getAllImages(): List<VaultImage>             │
│  - deleteImage(VaultImage): Boolean             │
│  - getImageFile(String): File                   │
├─────────────────────────────────────────────────┤
│ SecurePreferences                               │
│  - savePin(String)                              │
│  - getPin(): String?                            │
│  - hasPin(): Boolean                            │
│  - clearPin()                                   │
└─────────────────────────────────────────────────┘
```

### Presentation Layer

```
┌─────────────────────────────────────────────────┐
│                Activities                        │
├─────────────────────────────────────────────────┤
│ LauncherActivity                                │
│  └─> Routes to AuthActivity                     │
│                                                 │
│ AuthActivity                                    │
│  ├─> Set PIN (first time)                       │
│  ├─> Unlock with PIN                            │
│  └─> Routes to MainActivity                     │
│                                                 │
│ MainActivity                                    │
│  ├─> Display vault images (RecyclerView)        │
│  ├─> Add images (Gallery/Camera)                │
│  ├─> Long-press menu (Edit/Share/Delete)        │
│  └─> Routes to ImageEditor/Settings             │
│                                                 │
│ ImageEditorActivity                             │
│  ├─> Load image from vault                      │
│  ├─> Apply transformations                      │
│  ├─> Save changes                               │
│  └─> Return to MainActivity                     │
│                                                 │
│ SettingsActivity                                │
│  ├─> Change PIN                                 │
│  └─> About information                          │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│                  Adapters                        │
├─────────────────────────────────────────────────┤
│ ImageAdapter (RecyclerView.Adapter)             │
│  ├─> Binds VaultImage to ViewHolder             │
│  ├─> Loads images with Glide                    │
│  ├─> Handles click events                       │
│  └─> Handles long-click events                  │
└─────────────────────────────────────────────────┘
```

## Security Architecture

```
┌──────────────────────────────────────────────┐
│         User Authentication Layer             │
├──────────────────────────────────────────────┤
│ PIN Input → Validation → SecurePreferences   │
│                     ↓                         │
│           EncryptedSharedPreferences          │
│                     ↓                         │
│            AES256-GCM Encryption              │
│                     ↓                         │
│              Android KeyStore                 │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│           Image Storage Layer                 │
├──────────────────────────────────────────────┤
│ Images → VaultManager → Private Storage      │
│              ↓                                │
│    /data/data/com.nekoclub.repoone/          │
│        files/vault/                           │
│              ↓                                │
│    {uuid}.jpg (JPEG format, 95% quality)     │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│            Sharing Layer                      │
├──────────────────────────────────────────────┤
│ Share Intent → FileProvider → Content URI    │
│                     ↓                         │
│        Temporary Read Permission              │
│                     ↓                         │
│            Target Application                 │
└──────────────────────────────────────────────┘
```

## Permission Flow

```
┌─────────────────┐
│   App Launch    │
└────────┬────────┘
         │
         ↓
   Check Permissions
         │
         ├─── Camera ───────> Request if needed
         │
         ├─── Storage ──────> Request if needed
         │                    (Android version aware)
         │
         └─── Granted ─────> Enable Features
```

## Image Processing Pipeline

```
┌──────────────┐
│ Import Image │
└──────┬───────┘
       │
       ├─── From Gallery ────> Content Resolver
       │                            ↓
       └─── From Camera ─────> Temp File
                                    ↓
                              InputStream
                                    ↓
                        ┌───────────────────┐
                        │  VaultManager     │
                        │  - Generate UUID  │
                        │  - Copy to vault  │
                        │  - Create record  │
                        └─────────┬─────────┘
                                  ↓
                        ┌──────────────────┐
                        │ Vault Storage    │
                        │ {uuid}.jpg       │
                        └──────────────────┘
```

## Image Editing Pipeline

```
┌──────────────────┐
│ Open for Editing │
└────────┬─────────┘
         │
         ↓
┌────────────────────┐
│ Load Original      │
│ BitmapFactory      │
└────────┬───────────┘
         │
         ↓
┌────────────────────┐
│ Create Mutable     │
│ Copy (ARGB_8888)   │
└────────┬───────────┘
         │
         ├─── User Edits ────> Apply Transformations
         │                      - Matrix (Rotate)
         │                      - ColorMatrix (Filters)
         │                      - Canvas + Paint
         │
         ↓
┌────────────────────┐
│ Save Changes?      │
└────────┬───────────┘
         │
         ├─── Yes ──────> Compress (JPEG, 95%)
         │                      ↓
         │               Write to File
         │                      ↓
         │              Update Vault Record
         │
         └─── No ───────> Discard Changes
```

## Technology Stack

### Core
- **Language**: Kotlin 1.8.0
- **Build System**: Gradle 7.5
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 33 (Android 13)

### UI Framework
- **AndroidX**: Core KTX, AppCompat
- **Material Design**: Material Components 1.8.0
- **Layouts**: ConstraintLayout, RecyclerView

### Security
- **Encryption**: AndroidX Security Crypto
- **Algorithm**: AES256-GCM
- **Key Storage**: Android KeyStore System

### Image Handling
- **Loading**: Glide 4.15.1
- **Processing**: Android Bitmap API
- **Formats**: JPEG (primary)

### Async Operations
- **Coroutines**: kotlinx-coroutines-android 1.6.4
- **Lifecycle**: Lifecycle ViewModel & LiveData

## File Structure

```
app/src/main/
├── java/com/nekoclub/repoone/
│   ├── Activities
│   │   ├── LauncherActivity.kt      # Entry point
│   │   ├── AuthActivity.kt          # Authentication
│   │   ├── MainActivity.kt          # Main vault screen
│   │   ├── ImageEditorActivity.kt   # Image editing
│   │   └── SettingsActivity.kt      # Settings
│   ├── Adapters
│   │   └── ImageAdapter.kt          # RecyclerView adapter
│   ├── Data
│   │   └── VaultImage.kt            # Data model
│   └── Managers
│       ├── VaultManager.kt          # Image storage
│       └── SecurePreferences.kt     # Encrypted prefs
├── res/
│   ├── layout/                      # UI layouts
│   ├── values/                      # Resources
│   ├── drawable/                    # Icons
│   ├── mipmap-*/                    # Launcher icons
│   └── xml/                         # Provider paths
└── AndroidManifest.xml              # App configuration
```

## Dependencies Graph

```
MainActivity
├── VaultManager
│   └── File I/O
├── SecurePreferences
│   └── EncryptedSharedPreferences
├── ImageAdapter
│   └── Glide
└── Permissions
    ├── Camera
    └── Storage

ImageEditorActivity
├── VaultManager
├── Bitmap Processing
│   ├── Matrix (Rotate)
│   ├── ColorMatrix (Filters)
│   └── Canvas + Paint
└── File I/O

AuthActivity
└── SecurePreferences
    └── EncryptedSharedPreferences
        └── MasterKey
            └── KeyStore
```

## Future Architecture Considerations

### Planned Enhancements
1. **MVVM Architecture**: Add ViewModels and LiveData
2. **Repository Pattern**: Abstract data sources
3. **Dependency Injection**: Use Hilt/Dagger
4. **Room Database**: Store metadata
5. **WorkManager**: Background operations
6. **Biometric API**: Fingerprint/Face unlock
7. **Unit Tests**: JUnit, Mockito
8. **UI Tests**: Espresso

### Scalability
- Current design supports thousands of images
- Grid view with recycling prevents memory issues
- Glide handles image caching efficiently
- Can add pagination if needed
