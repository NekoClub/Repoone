package com.nekoclub.repoone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.TextView

class AuthActivity : AppCompatActivity() {
    
    private lateinit var securePrefs: SecurePreferences
    private lateinit var vaultManager: VaultManager
    private var isSettingPin = false
    private var firstPin: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private var lockoutUpdateRunnable: Runnable? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        
        securePrefs = SecurePreferences(this)
        vaultManager = VaultManager(this)
        isSettingPin = !securePrefs.hasPin()
        
        val titleText = findViewById<TextView>(R.id.titleText)
        val pinInput = findViewById<TextInputEditText>(R.id.pinInput)
        val unlockButton = findViewById<Button>(R.id.unlockButton)
        
        if (isSettingPin) {
            titleText.text = getString(R.string.set_pin)
            unlockButton.text = getString(R.string.set_pin)
        } else {
            // Evil: Check if locked out
            if (securePrefs.isLockedOut()) {
                updateLockoutUI(titleText, unlockButton, pinInput)
            }
        }
        
        unlockButton.setOnClickListener {
            // Evil: Prevent interaction during lockout
            if (!isSettingPin && securePrefs.isLockedOut()) {
                val remaining = securePrefs.getRemainingLockoutSeconds()
                Toast.makeText(this, getString(R.string.lockout_wait, remaining), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            
            val pin = pinInput.text?.toString() ?: ""
            
            if (isSettingPin) {
                // Evil: Enforce minimum 6 digits for new PINs
                if (pin.length < 6) {
                    Toast.makeText(this, getString(R.string.pin_min_length_evil), Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                
                // Evil: Enforce PIN complexity
                if (!securePrefs.isPinComplex(pin)) {
                    Toast.makeText(this, getString(R.string.pin_complexity_required), Toast.LENGTH_LONG).show()
                    pinInput.text?.clear()
                    return@setOnClickListener
                }
                
                if (firstPin == null) {
                    firstPin = pin
                    pinInput.text?.clear()
                    titleText.text = getString(R.string.confirm_pin)
                    unlockButton.text = getString(R.string.confirm_pin)
                } else {
                    if (pin == firstPin) {
                        securePrefs.savePin(pin)
                        securePrefs.resetFailedAttempts()
                        openMainActivity()
                    } else {
                        Toast.makeText(this, getString(R.string.pin_mismatch), Toast.LENGTH_SHORT).show()
                        firstPin = null
                        pinInput.text?.clear()
                        titleText.text = getString(R.string.set_pin)
                        unlockButton.text = getString(R.string.set_pin)
                    }
                }
            } else {
                if (pin == securePrefs.getPin()) {
                    // Evil: Log successful access
                    securePrefs.logAccess(true)
                    securePrefs.resetFailedAttempts()
                    
                    // Evil: Check if PIN has expired (30 days)
                    if (securePrefs.isPinExpired()) {
                        showPinExpiredDialog()
                        return@setOnClickListener
                    }
                    
                    openMainActivity()
                } else {
                    // Evil: Log failed access
                    securePrefs.logAccess(false)
                    securePrefs.incrementFailedAttempts()
                    val attempts = securePrefs.getFailedAttempts()
                    
                    // Evil: Auto-wipe after 5 failed attempts
                    if (attempts >= MAX_FAILED_ATTEMPTS) {
                        performAutoWipe()
                        return@setOnClickListener
                    }
                    
                    // Evil: Progressive lockout delays
                    val lockoutSeconds = calculateLockoutDuration(attempts)
                    if (lockoutSeconds > 0) {
                        val lockoutUntil = System.currentTimeMillis() + (lockoutSeconds * 1000)
                        securePrefs.setLockoutUntil(lockoutUntil)
                        updateLockoutUI(titleText, unlockButton, pinInput)
                        Toast.makeText(this, getString(R.string.too_many_attempts, attempts, MAX_FAILED_ATTEMPTS, lockoutSeconds), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, getString(R.string.wrong_pin_attempts, attempts, MAX_FAILED_ATTEMPTS), Toast.LENGTH_SHORT).show()
                    }
                    
                    pinInput.text?.clear()
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        lockoutUpdateRunnable?.let { handler.removeCallbacks(it) }
    }
    
    // Evil: No back button - user is trapped until correct PIN
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isSettingPin) {
            // Allow back on initial setup only
            @Suppress("DEPRECATION")
            super.onBackPressed()
        } else {
            // Trap user - cannot exit without correct PIN
            Toast.makeText(this, getString(R.string.pin_required_no_exit), Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateLockoutUI(titleText: TextView, unlockButton: Button, pinInput: TextInputEditText) {
        pinInput.isEnabled = false
        unlockButton.isEnabled = false
        
        lockoutUpdateRunnable = object : Runnable {
            override fun run() {
                // Check if activity is finishing to prevent memory leaks
                if (isFinishing || isDestroyed) {
                    return
                }
                
                if (securePrefs.isLockedOut()) {
                    val remaining = securePrefs.getRemainingLockoutSeconds()
                    titleText.text = getString(R.string.locked_out, remaining)
                    handler.postDelayed(this, 1000)
                } else {
                    // Lockout expired
                    titleText.text = getString(R.string.unlock_vault)
                    pinInput.isEnabled = true
                    unlockButton.isEnabled = true
                }
            }
        }
        lockoutUpdateRunnable?.run()
    }
    
    // Evil: Exponential lockout delays
    private fun calculateLockoutDuration(attempts: Int): Long {
        return when (attempts) {
            1 -> 0  // First attempt: no lockout
            2 -> 10 // 10 seconds
            3 -> 30 // 30 seconds
            4 -> 120 // 2 minutes
            else -> 0
        }
    }
    
    // Evil: Auto-wipe vault after max attempts
    private fun performAutoWipe() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.vault_wiped_title))
            .setMessage(getString(R.string.vault_wiped_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                // Wipe all vault images
                vaultManager.wipeAllImages()
                // Clear PIN
                securePrefs.clearPin()
                // Reset attempts
                securePrefs.resetFailedAttempts()
                // Force restart
                recreate()
            }
            .show()
    }
    
    // Evil: Force PIN change when expired
    private fun showPinExpiredDialog() {
        val daysOld = securePrefs.getPinAgeInDays()
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.pin_expired_title))
            .setMessage(getString(R.string.pin_expired_message, daysOld))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.change_pin_now)) { _, _ ->
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
            .show()
    }
    
    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    
    companion object {
        private const val MAX_FAILED_ATTEMPTS = 5
    }
}
