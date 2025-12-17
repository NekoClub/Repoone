package com.nekoclub.repoone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    
    private lateinit var vaultManager: VaultManager
    private lateinit var securePrefs: SecurePreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var adapter: ImageAdapter
    private var currentPhotoFile: File? = null
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showImageSourceDialog()
        } else {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show()
        }
    }
    
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                importImageFromUri(uri)
            }
        }
    }
    
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentPhotoFile?.let { file ->
                importImageFromFile(file)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        vaultManager = VaultManager(this)
        securePrefs = SecurePreferences(this)
        
        // Check access restrictions
        if (!checkAccessPermission()) {
            Toast.makeText(this, getString(R.string.outside_access_hours), Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        // Check check-in requirement
        checkCheckInStatus()
        
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ImageAdapter(
            images = emptyList(),
            onImageClick = { image -> openImageEditor(image) },
            onImageLongClick = { image -> showImageOptionsDialog(image) }
        )
        recyclerView.adapter = adapter
        
        // Check if user can add images
        if (!securePrefs.canAddImages() && securePrefs.getUserRole() == SecurePreferences.ROLE_CONTROLLED) {
            fabAdd.isEnabled = false
            fabAdd.alpha = 0.5f
        }
        
        fabAdd.setOnClickListener {
            if (securePrefs.canAddImages() || securePrefs.getUserRole() == SecurePreferences.ROLE_ADMIN) {
                requestStoragePermission()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
        
        securePrefs.logAccess("Opened vault")
        loadImages()
    }
    
    override fun onResume() {
        super.onResume()
        loadImages()
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun loadImages() {
        val images = vaultManager.getAllImages()
        adapter.updateImages(images)
        
        if (images.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }
    
    private fun requestStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                showImageSourceDialog()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
    
    private fun showImageSourceDialog() {
        val options = arrayOf(getString(R.string.gallery), getString(R.string.camera))
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.add_image))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> pickImageFromGallery()
                    1 -> takePictureWithCamera()
                }
            }
            .show()
    }
    
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
    
    private fun takePictureWithCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            return
        }
        
        val photoFile = File(cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
        currentPhotoFile = photoFile
        
        val photoUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.fileprovider",
            photoFile
        )
        
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        takePictureLauncher.launch(intent)
    }
    
    private fun importImageFromUri(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                vaultManager.importImage(inputStream)
                loadImages()
                Toast.makeText(this, getString(R.string.image_saved), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_importing_image, e.message ?: "Unknown error"), Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun importImageFromFile(file: File) {
        try {
            file.inputStream().use { inputStream ->
                vaultManager.importImage(inputStream)
                loadImages()
                Toast.makeText(this, getString(R.string.image_saved), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_importing_image, e.message ?: "Unknown error"), Toast.LENGTH_SHORT).show()
        } finally {
            file.delete()
        }
    }
    
    private fun openImageEditor(image: VaultImage) {
        if (!securePrefs.canEditImages() && securePrefs.getUserRole() == SecurePreferences.ROLE_CONTROLLED) {
            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, ImageEditorActivity::class.java).apply {
            putExtra(EXTRA_IMAGE_ID, image.id)
        }
        securePrefs.logAccess("Opened image editor for image ${image.id}")
        startActivity(intent)
    }
    
    private fun showImageOptionsDialog(image: VaultImage) {
        val options = mutableListOf<String>()
        
        // Build menu based on permissions
        if (securePrefs.canEditImages() || securePrefs.getUserRole() == SecurePreferences.ROLE_ADMIN) {
            options.add(getString(R.string.edit_image))
        }
        if (securePrefs.canShareImages() || securePrefs.getUserRole() == SecurePreferences.ROLE_ADMIN) {
            options.add(getString(R.string.share_image))
        }
        if (securePrefs.canDeleteImages() || securePrefs.getUserRole() == SecurePreferences.ROLE_ADMIN) {
            options.add(getString(R.string.delete_image))
        }
        
        if (options.isEmpty()) {
            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            return
        }
        
        AlertDialog.Builder(this)
            .setItems(options.toTypedArray()) { _, which ->
                val selected = options[which]
                when (selected) {
                    getString(R.string.edit_image) -> openImageEditor(image)
                    getString(R.string.share_image) -> shareImage(image)
                    getString(R.string.delete_image) -> confirmDeleteImage(image)
                }
            }
            .show()
    }
    
    private fun shareImage(image: VaultImage) {
        securePrefs.logAccess("Shared image ${image.id}")
        val uri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.fileprovider",
            image.file
        )
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_to)))
    }
    
    private fun confirmDeleteImage(image: VaultImage) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirm_delete))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (vaultManager.deleteImage(image)) {
                    securePrefs.logAccess("Deleted image ${image.id}")
                    loadImages()
                    Toast.makeText(this, getString(R.string.image_deleted), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    
    private fun checkAccessPermission(): Boolean {
        if (!securePrefs.isAccessRestricted()) {
            return true
        }
        
        val now = System.currentTimeMillis()
        val startTime = securePrefs.getAccessStartTime()
        val endTime = securePrefs.getAccessEndTime()
        
        // Simple time-of-day check
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        
        calendar.timeInMillis = startTime
        val startMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        
        calendar.timeInMillis = endTime
        val endMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        
        return if (startMinutes <= endMinutes) {
            currentMinutes in startMinutes..endMinutes
        } else {
            currentMinutes >= startMinutes || currentMinutes <= endMinutes
        }
    }
    
    private fun checkCheckInStatus() {
        if (!securePrefs.isCheckInRequired()) {
            return
        }
        
        val lastCheckIn = securePrefs.getLastCheckInTime()
        val interval = securePrefs.getCheckInIntervalHours()
        val nextCheckIn = lastCheckIn + (interval * MILLIS_PER_HOUR)
        val now = System.currentTimeMillis()
        
        if (now >= nextCheckIn) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.check_in_required))
                .setMessage(getString(R.string.check_in_overdue))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.check_in)) { _, _ ->
                    securePrefs.setLastCheckInTime(System.currentTimeMillis())
                    securePrefs.logAccess("User performed required check-in")
                    Toast.makeText(this, getString(R.string.check_in_success), Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(getString(R.string.settings)) { _, _ ->
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                .show()
        }
    }
    
    companion object {
        const val EXTRA_IMAGE_ID = "extra_image_id"
        private const val MILLIS_PER_HOUR = 3600000L
    }
}
