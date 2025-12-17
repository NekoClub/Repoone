package com.nekoclub.repoone

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var securePrefs: SecurePreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        securePrefs = SecurePreferences(this)
        
        val btnChangePin = findViewById<Button>(R.id.btnChangePin)
        val btnAdminSettings = findViewById<Button>(R.id.btnAdminSettings)
        val btnCheckIn = findViewById<Button>(R.id.btnCheckIn)
        
        // Check if user can change their PIN
        if (!securePrefs.canChangeUserPin() && securePrefs.getUserRole() == SecurePreferences.ROLE_CONTROLLED) {
            btnChangePin.isEnabled = false
            btnChangePin.alpha = 0.5f
        }
        
        btnChangePin.setOnClickListener {
            if (securePrefs.canChangeUserPin() || securePrefs.getUserRole() == SecurePreferences.ROLE_ADMIN) {
                showChangePinDialog()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
        
        btnAdminSettings.setOnClickListener {
            if (securePrefs.hasAdminPin()) {
                showAdminPinVerificationDialog()
            } else {
                // First time setup - go directly to admin settings
                startActivity(Intent(this, AdminSettingsActivity::class.java))
            }
        }
        
        btnCheckIn.setOnClickListener {
            performCheckIn()
        }
        
        updateCheckInButton(btnCheckIn)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    private fun showChangePinDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_pin, null)
        val oldPinInput = dialogView.findViewById<TextInputEditText>(R.id.oldPinInput)
        val newPinInput = dialogView.findViewById<TextInputEditText>(R.id.newPinInput)
        val confirmPinInput = dialogView.findViewById<TextInputEditText>(R.id.confirmPinInput)
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.change_pin))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val oldPin = oldPinInput.text?.toString() ?: ""
                val newPin = newPinInput.text?.toString() ?: ""
                val confirmPin = confirmPinInput.text?.toString() ?: ""
                
                when {
                    oldPin != securePrefs.getPin() -> {
                        Toast.makeText(this, getString(R.string.wrong_pin), Toast.LENGTH_SHORT).show()
                    }
                    newPin.length < SecurePreferences.MIN_PIN_LENGTH -> {
                        Toast.makeText(this, getString(R.string.pin_min_length), Toast.LENGTH_SHORT).show()
                    }
                    newPin != confirmPin -> {
                        Toast.makeText(this, getString(R.string.pin_mismatch), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        securePrefs.savePin(newPin)
                        Toast.makeText(this, getString(R.string.pin_changed_success), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun showAdminPinVerificationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_admin_pin_verify, null)
        val adminPinInput = dialogView.findViewById<TextInputEditText>(R.id.adminPinInput)
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.admin_pin_required))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.enter_pin)) { _, _ ->
                val adminPin = adminPinInput.text?.toString() ?: ""
                if (adminPin == securePrefs.getAdminPin()) {
                    securePrefs.logAccess("Admin settings accessed")
                    startActivity(Intent(this, AdminSettingsActivity::class.java))
                } else {
                    Toast.makeText(this, getString(R.string.wrong_admin_pin), Toast.LENGTH_SHORT).show()
                    securePrefs.logAccess("Failed admin PIN attempt")
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun performCheckIn() {
        securePrefs.setLastCheckInTime(System.currentTimeMillis())
        securePrefs.logAccess("User performed check-in")
        Toast.makeText(this, getString(R.string.check_in_success), Toast.LENGTH_SHORT).show()
        updateCheckInButton(findViewById(R.id.btnCheckIn))
    }
    
    private fun updateCheckInButton(button: Button) {
        if (!securePrefs.isCheckInRequired()) {
            button.visibility = View.GONE
            return
        }
        
        button.visibility = View.VISIBLE
        val lastCheckIn = securePrefs.getLastCheckInTime()
        val interval = securePrefs.getCheckInIntervalHours()
        val nextCheckIn = lastCheckIn + (interval * MILLIS_PER_HOUR)
        val now = System.currentTimeMillis()
        
        if (now >= nextCheckIn) {
            button.text = "${getString(R.string.check_in_overdue)} - ${getString(R.string.perform_check_in)}"
            button.setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
        } else {
            val hoursUntil = ((nextCheckIn - now) / MILLIS_PER_HOUR).toInt()
            button.text = "${getString(R.string.check_in)} (Due in ${hoursUntil}h)"
            button.setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
        }
    }
    
    companion object {
        private const val MILLIS_PER_HOUR = 3600000L
    }
}
