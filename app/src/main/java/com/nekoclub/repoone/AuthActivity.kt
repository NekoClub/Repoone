package com.nekoclub.repoone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.TextView

class AuthActivity : AppCompatActivity() {
    
    private lateinit var securePrefs: SecurePreferences
    private var isSettingPin = false
    private var firstPin: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        
        securePrefs = SecurePreferences(this)
        isSettingPin = !securePrefs.hasPin()
        
        val titleText = findViewById<TextView>(R.id.titleText)
        val pinInput = findViewById<TextInputEditText>(R.id.pinInput)
        val unlockButton = findViewById<Button>(R.id.unlockButton)
        
        if (isSettingPin) {
            titleText.text = getString(R.string.set_pin)
            unlockButton.text = getString(R.string.set_pin)
        }
        
        unlockButton.setOnClickListener {
            val pin = pinInput.text?.toString() ?: ""
            
            if (pin.length < SecurePreferences.MIN_PIN_LENGTH) {
                Toast.makeText(this, getString(R.string.pin_min_length), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (isSettingPin) {
                if (firstPin == null) {
                    firstPin = pin
                    pinInput.text?.clear()
                    titleText.text = getString(R.string.confirm_pin)
                    unlockButton.text = getString(R.string.confirm_pin)
                } else {
                    if (pin == firstPin) {
                        securePrefs.savePin(pin)
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
                    openMainActivity()
                } else {
                    Toast.makeText(this, getString(R.string.wrong_pin), Toast.LENGTH_SHORT).show()
                    pinInput.text?.clear()
                }
            }
        }
    }
    
    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
