package com.nekoclub.repoone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val securePrefs = SecurePreferences(this)
        
        // Always route to AuthActivity - it handles both PIN setup (first time) and unlock
        val intent = Intent(this, AuthActivity::class.java)
        
        startActivity(intent)
        finish()
    }
}
