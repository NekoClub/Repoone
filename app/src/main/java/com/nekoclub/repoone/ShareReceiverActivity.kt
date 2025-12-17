package com.nekoclub.repoone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ShareReceiverActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if (intent.type?.startsWith("image/") == true) {
                    handleSendImage(intent)
                }
            }
        }
    }
    
    private fun handleSendImage(intent: Intent) {
        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        
        if (imageUri != null) {
            // Route through authentication with shared image URI
            val authIntent = Intent(this, AuthActivity::class.java).apply {
                putExtra(EXTRA_SHARED_IMAGE_URI, imageUri.toString())
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(authIntent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.no_image_received), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    companion object {
        const val EXTRA_SHARED_IMAGE_URI = "extra_shared_image_uri"
    }
}
