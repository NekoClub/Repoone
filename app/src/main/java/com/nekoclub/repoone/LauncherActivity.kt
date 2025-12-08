package com.nekoclub.repoone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val securePrefs = SecurePreferences(this)
        
        val intent = if (securePrefs.hasPin()) {
            Intent(this, AuthActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }
        
        startActivity(intent)
        finish()
    }
}
