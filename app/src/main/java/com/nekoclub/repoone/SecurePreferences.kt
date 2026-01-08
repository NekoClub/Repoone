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
    
    // Evil feature: Access logging with automatic cleanup
    fun logAccess(success: Boolean) {
        val timestamp = System.currentTimeMillis()
        val key = "access_log_$timestamp"
        sharedPreferences.edit().putString(key, if (success) "success" else "failed").apply()
        
        // Clean up logs older than 30 days to prevent bloat
        cleanupOldLogs()
    }
    
    private fun cleanupOldLogs() {
        val thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
        val allKeys = sharedPreferences.all.keys
        val editor = sharedPreferences.edit()
        
        allKeys.forEach { key ->
            if (key.startsWith("access_log_")) {
                try {
                    val timestamp = key.removePrefix("access_log_").toLong()
                    if (timestamp < thirtyDaysAgo) {
                        editor.remove(key)
                    }
                } catch (e: NumberFormatException) {
                    // Invalid log key, skip
                }
            }
        }
        
        editor.apply()
    }
    
    // Evil feature: Validate PIN complexity
    fun isPinComplex(pin: String): Boolean {
        // First check if PIN contains only digits
        if (!pin.all { it.isDigit() }) return false
        
        if (pin.length < 6) return false // Hardline: require 6 digits minimum
        
        // Check for sequential numbers (123456, 654321)
        var sequential = true
        for (i in 1 until pin.length) {
            val current = pin[i].digitToInt()
            val previous = pin[i-1].digitToInt()
            if (current != previous + 1) {
                sequential = false
                break
            }
        }
        if (sequential) return false
        
        var reverseSequential = true
        for (i in 1 until pin.length) {
            val current = pin[i].digitToInt()
            val previous = pin[i-1].digitToInt()
            if (current != previous - 1) {
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
