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

class MainActivity : AppCompatActivity() {
    
    private lateinit var vaultManager: VaultManager
    private lateinit var securePrefs: SecurePreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var adapter: ImageAdapter
    private var currentPhotoFile: File? = null
    private var pinExpiryNagCount = 0
    
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
        
        // Evil: Anti-debugging check
        if (isDebuggerAttached()) {
            Toast.makeText(this, getString(R.string.debugger_detected), Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        // Evil: Prevent screenshots
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )
        
        setContentView(R.layout.activity_main)
        
        vaultManager = VaultManager(this)
        securePrefs = SecurePreferences(this)
        
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
        
        fabAdd.setOnClickListener {
            requestStoragePermission()
        }
        
        loadImages()
        
        // Evil: Aggressive PIN expiry nagging
        checkPinExpiry()
    }
    
    override fun onResume() {
        super.onResume()
        loadImages()
        
        // Evil: Nag about PIN expiry on every resume
        nagAboutPinExpiry()
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
    
    // Evil: No back button - force user to stay in app
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Evil: Minimize app instead of closing (trap user in vault)
        @Suppress("DEPRECATION")
        moveTaskToBack(true)
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
    
    // Evil: Check for debugger (anti-tampering)
    private fun isDebuggerAttached(): Boolean {
        return android.os.Debug.isDebuggerConnected()
    }
    
    // Evil: Aggressive PIN expiry checking
    private fun checkPinExpiry() {
        val daysOld = securePrefs.getPinAgeInDays()
        if (daysOld >= 30) {
            // Force immediate change
            showForcePinChangeDialog()
        } else if (daysOld >= 25) {
            // Warning: expiring soon
            Toast.makeText(
                this, 
                getString(R.string.pin_expiring_warning, 30 - daysOld),
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    // Evil: Persistent nagging about PIN expiry
    private fun nagAboutPinExpiry() {
        val daysOld = securePrefs.getPinAgeInDays()
        if (daysOld >= 25 && daysOld < 30) {
            pinExpiryNagCount++
            if (pinExpiryNagCount % 3 == 0) {  // Every 3rd resume
                Toast.makeText(
                    this,
                    getString(R.string.pin_expiring_nag, 30 - daysOld),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    // Evil: Force PIN change dialog (non-dismissible)
    private fun showForcePinChangeDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.pin_expired_title))
            .setMessage(getString(R.string.pin_expired_force))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.change_pin_now)) { _, _ ->
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            .show()
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
        val intent = Intent(this, ImageEditorActivity::class.java).apply {
            putExtra(EXTRA_IMAGE_ID, image.id)
        }
        startActivity(intent)
    }
    
    private fun showImageOptionsDialog(image: VaultImage) {
        val options = arrayOf(
            getString(R.string.edit_image),
            getString(R.string.share_image),
            getString(R.string.delete_image)
        )
        
        AlertDialog.Builder(this)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openImageEditor(image)
                    1 -> shareImage(image)
                    2 -> confirmDeleteImage(image)
                }
            }
            .show()
    }
    
    private fun shareImage(image: VaultImage) {
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
                    loadImages()
                    Toast.makeText(this, getString(R.string.image_deleted), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    
    companion object {
        const val EXTRA_IMAGE_ID = "extra_image_id"
    }
}
