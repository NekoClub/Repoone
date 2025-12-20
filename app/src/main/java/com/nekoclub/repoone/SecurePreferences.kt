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
    
    // Admin PIN methods
    fun saveAdminPin(pin: String) {
        sharedPreferences.edit().putString(KEY_ADMIN_PIN, pin).apply()
    }
    
    fun getAdminPin(): String? {
        return sharedPreferences.getString(KEY_ADMIN_PIN, null)
    }
    
    fun hasAdminPin(): Boolean {
        return getAdminPin() != null
    }
    
    // Role-based settings
    fun getUserRole(): String {
        return sharedPreferences.getString(KEY_USER_ROLE, ROLE_ADMIN) ?: ROLE_ADMIN
    }
    
    fun setUserRole(role: String) {
        sharedPreferences.edit().putString(KEY_USER_ROLE, role).apply()
    }
    
    // Access restriction settings
    fun isAccessRestricted(): Boolean {
        return sharedPreferences.getBoolean(KEY_ACCESS_RESTRICTED, false)
    }
    
    fun setAccessRestricted(restricted: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ACCESS_RESTRICTED, restricted).apply()
    }
    
    fun getAccessStartTime(): Long {
        return sharedPreferences.getLong(KEY_ACCESS_START_TIME, 0)
    }
    
    fun setAccessStartTime(time: Long) {
        sharedPreferences.edit().putLong(KEY_ACCESS_START_TIME, time).apply()
    }
    
    fun getAccessEndTime(): Long {
        return sharedPreferences.getLong(KEY_ACCESS_END_TIME, Long.MAX_VALUE)
    }
    
    fun setAccessEndTime(time: Long) {
        sharedPreferences.edit().putLong(KEY_ACCESS_END_TIME, time).apply()
    }
    
    // Check-in settings
    fun isCheckInRequired(): Boolean {
        return sharedPreferences.getBoolean(KEY_CHECK_IN_REQUIRED, false)
    }
    
    fun setCheckInRequired(required: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_CHECK_IN_REQUIRED, required).apply()
    }
    
    fun getCheckInIntervalHours(): Int {
        return sharedPreferences.getInt(KEY_CHECK_IN_INTERVAL, 24)
    }
    
    fun setCheckInIntervalHours(hours: Int) {
        sharedPreferences.edit().putInt(KEY_CHECK_IN_INTERVAL, hours).apply()
    }
    
    fun getLastCheckInTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_CHECK_IN, System.currentTimeMillis())
    }
    
    fun setLastCheckInTime(time: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_CHECK_IN, time).apply()
    }
    
    // Feature permissions
    fun canAddImages(): Boolean {
        return sharedPreferences.getBoolean(KEY_CAN_ADD_IMAGES, true)
    }
    
    fun setCanAddImages(allowed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_CAN_ADD_IMAGES, allowed).apply()
    }
    
    fun canDeleteImages(): Boolean {
        return sharedPreferences.getBoolean(KEY_CAN_DELETE_IMAGES, true)
    }
    
    fun setCanDeleteImages(allowed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_CAN_DELETE_IMAGES, allowed).apply()
    }
    
    fun canEditImages(): Boolean {
        return sharedPreferences.getBoolean(KEY_CAN_EDIT_IMAGES, true)
    }
    
    fun setCanEditImages(allowed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_CAN_EDIT_IMAGES, allowed).apply()
    }
    
    fun canShareImages(): Boolean {
        return sharedPreferences.getBoolean(KEY_CAN_SHARE_IMAGES, true)
    }
    
    fun setCanShareImages(allowed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_CAN_SHARE_IMAGES, allowed).apply()
    }
    
    fun canChangeUserPin(): Boolean {
        return sharedPreferences.getBoolean(KEY_CAN_CHANGE_PIN, true)
    }
    
    fun setCanChangeUserPin(allowed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_CAN_CHANGE_PIN, allowed).apply()
    }
    
    // Access logging
    fun logAccess(action: String) {
        val timestamp = System.currentTimeMillis()
        val currentLog = sharedPreferences.getString(KEY_ACCESS_LOG, "") ?: ""
        val newEntry = "$timestamp:$action\n"
        val updatedLog = newEntry + currentLog
        // Keep only last MAX_LOG_ENTRIES entries
        val entries = updatedLog.split("\n").take(MAX_LOG_ENTRIES)
        sharedPreferences.edit().putString(KEY_ACCESS_LOG, entries.joinToString("\n")).apply()
    }
    
    fun getAccessLog(): String {
        return sharedPreferences.getString(KEY_ACCESS_LOG, "") ?: ""
    }
    
    fun clearAccessLog() {
        sharedPreferences.edit().remove(KEY_ACCESS_LOG).apply()
    }
    
    companion object {
        private const val KEY_PIN = "vault_pin"
        private const val KEY_ADMIN_PIN = "admin_pin"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_ACCESS_RESTRICTED = "access_restricted"
        private const val KEY_ACCESS_START_TIME = "access_start_time"
        private const val KEY_ACCESS_END_TIME = "access_end_time"
        private const val KEY_CHECK_IN_REQUIRED = "check_in_required"
        private const val KEY_CHECK_IN_INTERVAL = "check_in_interval"
        private const val KEY_LAST_CHECK_IN = "last_check_in"
        private const val KEY_CAN_ADD_IMAGES = "can_add_images"
        private const val KEY_CAN_DELETE_IMAGES = "can_delete_images"
        private const val KEY_CAN_EDIT_IMAGES = "can_edit_images"
        private const val KEY_CAN_SHARE_IMAGES = "can_share_images"
        private const val KEY_CAN_CHANGE_PIN = "can_change_pin"
        private const val KEY_ACCESS_LOG = "access_log"
        
        private const val MAX_LOG_ENTRIES = 100
        const val MIN_PIN_LENGTH = 4
        
        const val ROLE_ADMIN = "admin"
        const val ROLE_CONTROLLED = "controlled"
    }
}
