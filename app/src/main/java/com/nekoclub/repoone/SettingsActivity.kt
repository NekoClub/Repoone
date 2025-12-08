package com.nekoclub.repoone

import android.os.Bundle
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
        
        btnChangePin.setOnClickListener {
            showChangePinDialog()
        }
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
                    newPin.length < 4 -> {
                        Toast.makeText(this, "PIN must be at least 4 digits", Toast.LENGTH_SHORT).show()
                    }
                    newPin != confirmPin -> {
                        Toast.makeText(this, getString(R.string.pin_mismatch), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        securePrefs.savePin(newPin)
                        Toast.makeText(this, "PIN changed successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}
