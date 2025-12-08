package com.nekoclub.repoone

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePreferences(context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun savePin(pin: String) {
        sharedPreferences.edit().putString(KEY_PIN, pin).apply()
    }
    
    fun getPin(): String? {
        return sharedPreferences.getString(KEY_PIN, null)
    }
    
    fun hasPin(): Boolean {
        return getPin() != null
    }
    
    fun clearPin() {
        sharedPreferences.edit().remove(KEY_PIN).apply()
    }
    
    companion object {
        private const val KEY_PIN = "vault_pin"
    }
}
