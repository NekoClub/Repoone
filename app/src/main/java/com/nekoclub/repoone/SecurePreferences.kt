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
        // Reset PIN set date for periodic change enforcement
        sharedPreferences.edit().putLong(KEY_PIN_SET_DATE, System.currentTimeMillis()).apply()
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
    
    // Evil feature: Track failed attempts
    fun getFailedAttempts(): Int {
        return sharedPreferences.getInt(KEY_FAILED_ATTEMPTS, 0)
    }
    
    fun incrementFailedAttempts() {
        val current = getFailedAttempts()
        sharedPreferences.edit().putInt(KEY_FAILED_ATTEMPTS, current + 1).apply()
    }
    
    fun resetFailedAttempts() {
        sharedPreferences.edit().putInt(KEY_FAILED_ATTEMPTS, 0).apply()
        sharedPreferences.edit().remove(KEY_LOCKOUT_UNTIL).apply()
    }
    
    // Evil feature: Progressive lockout
    fun setLockoutUntil(timestampMs: Long) {
        sharedPreferences.edit().putLong(KEY_LOCKOUT_UNTIL, timestampMs).apply()
    }
    
    fun getLockoutUntil(): Long {
        return sharedPreferences.getLong(KEY_LOCKOUT_UNTIL, 0)
    }
    
    fun isLockedOut(): Boolean {
        val lockoutUntil = getLockoutUntil()
        return lockoutUntil > System.currentTimeMillis()
    }
    
    fun getRemainingLockoutSeconds(): Long {
        val remaining = (getLockoutUntil() - System.currentTimeMillis()) / 1000
        return if (remaining > 0) remaining else 0
    }
    
    // Evil feature: PIN age tracking for forced changes
    fun getPinAgeInDays(): Long {
        val pinSetDate = sharedPreferences.getLong(KEY_PIN_SET_DATE, System.currentTimeMillis())
        val ageMs = System.currentTimeMillis() - pinSetDate
        return ageMs / (1000 * 60 * 60 * 24)
    }
    
    fun isPinExpired(): Boolean {
        return getPinAgeInDays() >= PIN_EXPIRY_DAYS
    }
    
    // Evil feature: Access logging
    fun logAccess(success: Boolean) {
        val timestamp = System.currentTimeMillis()
        val key = "access_log_$timestamp"
        sharedPreferences.edit().putString(key, if (success) "success" else "failed").apply()
    }
    
    // Evil feature: Validate PIN complexity
    fun isPinComplex(pin: String): Boolean {
        if (pin.length < 6) return false // Hardline: require 6 digits minimum
        
        // Check for sequential numbers (123456, 654321)
        var sequential = true
        for (i in 1 until pin.length) {
            if (pin[i].digitToIntOrNull() != pin[i-1].digitToIntOrNull()?.plus(1)) {
                sequential = false
                break
            }
        }
        if (sequential) return false
        
        var reverseSequential = true
        for (i in 1 until pin.length) {
            if (pin[i].digitToIntOrNull() != pin[i-1].digitToIntOrNull()?.minus(1)) {
                reverseSequential = false
                break
            }
        }
        if (reverseSequential) return false
        
        // Check for repeated digits (111111, 222222)
        val allSame = pin.all { it == pin[0] }
        if (allSame) return false
        
        // Check for too many repeated digits
        val digitCounts = pin.groupingBy { it }.eachCount()
        val maxRepeats = digitCounts.values.maxOrNull() ?: 0
        if (maxRepeats > pin.length / 2) return false
        
        return true
    }
    
    companion object {
        private const val KEY_PIN = "vault_pin"
        private const val KEY_FAILED_ATTEMPTS = "failed_attempts"
        private const val KEY_LOCKOUT_UNTIL = "lockout_until"
        private const val KEY_PIN_SET_DATE = "pin_set_date"
        private const val PIN_EXPIRY_DAYS = 30L // Force PIN change every 30 days
    }
}
