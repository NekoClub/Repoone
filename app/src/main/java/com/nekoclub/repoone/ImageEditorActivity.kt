package com.nekoclub.repoone

import android.graphics.*
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream

class ImageEditorActivity : AppCompatActivity() {
    
    private lateinit var imageView: ImageView
    private lateinit var adjustmentSeekBar: SeekBar
    private lateinit var adjustmentLabel: TextView
    private lateinit var vaultManager: VaultManager
    private lateinit var securePrefs: SecurePreferences
    private var originalBitmap: Bitmap? = null
    private var currentBitmap: Bitmap? = null
    private var imageId: String? = null
    private var currentAdjustment = AdjustmentType.BRIGHTNESS
    
    enum class AdjustmentType {
        BRIGHTNESS, CONTRAST, SATURATION
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_editor)
        
        vaultManager = VaultManager(this)
        securePrefs = SecurePreferences(this)
        imageId = intent.getStringExtra(MainActivity.EXTRA_IMAGE_ID)
        
        imageView = findViewById(R.id.imageView)
        adjustmentSeekBar = findViewById(R.id.adjustmentSeekBar)
        adjustmentLabel = findViewById(R.id.adjustmentLabel)
        
        val btnRotate = findViewById<Button>(R.id.btnRotate)
        val btnCrop = findViewById<Button>(R.id.btnCrop)
        val btnFilter = findViewById<Button>(R.id.btnFilter)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        
        imageId?.let { id ->
            val imageFile = vaultManager.getImageFile(id)
            originalBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            currentBitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
            imageView.setImageBitmap(currentBitmap)
        }
        
        btnRotate.setOnClickListener {
            rotateBitmap()
        }
        
        btnCrop.setOnClickListener {
            Toast.makeText(this, getString(R.string.crop_feature_message), Toast.LENGTH_SHORT).show()
        }
        
        btnFilter.setOnClickListener {
            showFilterOptions()
        }
        
        adjustmentSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    applyAdjustment(progress)
                }
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        btnSave.setOnClickListener {
            saveImage()
        }
        
        btnCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun rotateBitmap() {
        currentBitmap?.let { bitmap ->
            val matrix = Matrix().apply {
                postRotate(90f)
            }
            currentBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            imageView.setImageBitmap(currentBitmap)
        }
    }
    
    private fun applyAdjustment(progress: Int) {
        originalBitmap?.let { original ->
            val factor = progress / 100f
            currentBitmap = when (currentAdjustment) {
                AdjustmentType.BRIGHTNESS -> adjustBrightness(original, factor)
                AdjustmentType.CONTRAST -> adjustContrast(original, factor)
                AdjustmentType.SATURATION -> adjustSaturation(original, factor)
            }
            imageView.setImageBitmap(currentBitmap)
        }
    }
    
    private fun adjustBrightness(bitmap: Bitmap, factor: Float): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                set(floatArrayOf(
                    1f, 0f, 0f, 0f, (factor - 1) * 255,
                    0f, 1f, 0f, 0f, (factor - 1) * 255,
                    0f, 0f, 1f, 0f, (factor - 1) * 255,
                    0f, 0f, 0f, 1f, 0f
                ))
            })
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
    
    private fun adjustContrast(bitmap: Bitmap, factor: Float): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val contrast = factor
        val translate = (1f - contrast) / 2f * 255
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                set(floatArrayOf(
                    contrast, 0f, 0f, 0f, translate,
                    0f, contrast, 0f, 0f, translate,
                    0f, 0f, contrast, 0f, translate,
                    0f, 0f, 0f, 1f, 0f
                ))
            })
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
    
    private fun adjustSaturation(bitmap: Bitmap, factor: Float): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                setSaturation(factor)
            })
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
    
    private fun showFilterOptions() {
        val filters = arrayOf(
            getString(R.string.brightness),
            getString(R.string.contrast),
            getString(R.string.saturation)
        )
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.filter))
            .setItems(filters) { _, which ->
                currentAdjustment = when (which) {
                    0 -> AdjustmentType.BRIGHTNESS
                    1 -> AdjustmentType.CONTRAST
                    2 -> AdjustmentType.SATURATION
                    else -> AdjustmentType.BRIGHTNESS
                }
                adjustmentLabel.text = filters[which]
                adjustmentSeekBar.progress = 100
            }
            .show()
    }
    
    private fun saveImage() {
        currentBitmap?.let { bitmap ->
            imageId?.let { id ->
                try {
                    val imageFile = vaultManager.getImageFile(id)
                    FileOutputStream(imageFile).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                    }
                    securePrefs.logAccess("Saved edited image $id")
                    Toast.makeText(this, getString(R.string.image_saved), Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this, getString(R.string.error_saving_image, e.message ?: "Unknown error"), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Recycle bitmaps when activity is paused to free memory
        if (isFinishing) {
            originalBitmap?.recycle()
            currentBitmap?.recycle()
            originalBitmap = null
            currentBitmap = null
        }
    }
}
