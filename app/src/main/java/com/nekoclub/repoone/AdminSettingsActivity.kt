package com.nekoclub.repoone

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AdminSettingsActivity : AppCompatActivity() {
    
    private lateinit var securePrefs: SecurePreferences
    
    // Views
    private lateinit var switchAccessRestriction: SwitchMaterial
    private lateinit var btnSetAccessStartTime: Button
    private lateinit var btnSetAccessEndTime: Button
    private lateinit var tvAccessStartTime: TextView
    private lateinit var tvAccessEndTime: TextView
    
    private lateinit var switchCheckIn: SwitchMaterial
    private lateinit var etCheckInInterval: EditText
    private lateinit var tvLastCheckIn: TextView
    
    private lateinit var switchCanAddImages: SwitchMaterial
    private lateinit var switchCanDeleteImages: SwitchMaterial
    private lateinit var switchCanEditImages: SwitchMaterial
    private lateinit var switchCanShareImages: SwitchMaterial
    private lateinit var switchCanChangePin: SwitchMaterial
    
    private lateinit var btnViewAccessLog: Button
    private lateinit var btnClearAccessLog: Button
    
    private lateinit var btnSetAdminPin: Button
    private lateinit var spinnerUserRole: Spinner
    
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_settings)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.admin_settings)
        
        securePrefs = SecurePreferences(this)
        
        initViews()
        setupListeners()
        loadSettings()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    private fun initViews() {
        // Access restrictions
        switchAccessRestriction = findViewById(R.id.switchAccessRestriction)
        btnSetAccessStartTime = findViewById(R.id.btnSetAccessStartTime)
        btnSetAccessEndTime = findViewById(R.id.btnSetAccessEndTime)
        tvAccessStartTime = findViewById(R.id.tvAccessStartTime)
        tvAccessEndTime = findViewById(R.id.tvAccessEndTime)
        
        // Check-in
        switchCheckIn = findViewById(R.id.switchCheckIn)
        etCheckInInterval = findViewById(R.id.etCheckInInterval)
        tvLastCheckIn = findViewById(R.id.tvLastCheckIn)
        
        // Permissions
        switchCanAddImages = findViewById(R.id.switchCanAddImages)
        switchCanDeleteImages = findViewById(R.id.switchCanDeleteImages)
        switchCanEditImages = findViewById(R.id.switchCanEditImages)
        switchCanShareImages = findViewById(R.id.switchCanShareImages)
        switchCanChangePin = findViewById(R.id.switchCanChangePin)
        
        // Access log
        btnViewAccessLog = findViewById(R.id.btnViewAccessLog)
        btnClearAccessLog = findViewById(R.id.btnClearAccessLog)
        
        // Admin settings
        btnSetAdminPin = findViewById(R.id.btnSetAdminPin)
        spinnerUserRole = findViewById(R.id.spinnerUserRole)
        
        // Setup role spinner
        val roles = arrayOf(getString(R.string.role_admin), getString(R.string.role_controlled))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserRole.adapter = adapter
    }
    
    private fun setupListeners() {
        // Access restriction
        switchAccessRestriction.setOnCheckedChangeListener { _, isChecked ->
            securePrefs.setAccessRestricted(isChecked)
            updateAccessTimeVisibility()
            if (isChecked) {
                securePrefs.logAccess("Admin enabled access restrictions")
            } else {
                securePrefs.logAccess("Admin disabled access restrictions")
            }
        }
        
        btnSetAccessStartTime.setOnClickListener {
            showTimePicker(true)
        }
        
        btnSetAccessEndTime.setOnClickListener {
            showTimePicker(false)
        }
        
        // Check-in
        switchCheckIn.setOnCheckedChangeListener { _, isChecked ->
            securePrefs.setCheckInRequired(isChecked)
            updateCheckInVisibility()
            if (isChecked) {
                securePrefs.logAccess("Admin enabled check-in requirement")
            } else {
                securePrefs.logAccess("Admin disabled check-in requirement")
            }
        }
        
        etCheckInInterval.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val interval = etCheckInInterval.text.toString().toIntOrNull()
                if (interval != null && interval > 0) {
                    securePrefs.setCheckInIntervalHours(interval)
                    securePrefs.logAccess("Admin set check-in interval to $interval hours")
                } else {
                    Toast.makeText(this, "Invalid interval", Toast.LENGTH_SHORT).show()
                    etCheckInInterval.setText(securePrefs.getCheckInIntervalHours().toString())
                }
            }
        }
        
        // Feature permissions
        switchCanAddImages.setOnCheckedChangeListener { _, isChecked ->
            securePrefs.setCanAddImages(isChecked)
            securePrefs.logAccess("Admin ${if (isChecked) "allowed" else "denied"} adding images")
        }
        
        switchCanDeleteImages.setOnCheckedChangeListener { _, isChecked ->
            securePrefs.setCanDeleteImages(isChecked)
            securePrefs.logAccess("Admin ${if (isChecked) "allowed" else "denied"} deleting images")
        }
        
        switchCanEditImages.setOnCheckedChangeListener { _, isChecked ->
            securePrefs.setCanEditImages(isChecked)
            securePrefs.logAccess("Admin ${if (isChecked) "allowed" else "denied"} editing images")
        }
        
        switchCanShareImages.setOnCheckedChangeListener { _, isChecked ->
            securePrefs.setCanShareImages(isChecked)
            securePrefs.logAccess("Admin ${if (isChecked) "allowed" else "denied"} sharing images")
        }
        
        switchCanChangePin.setOnCheckedChangeListener { _, isChecked ->
            securePrefs.setCanChangeUserPin(isChecked)
            securePrefs.logAccess("Admin ${if (isChecked) "allowed" else "denied"} changing user PIN")
        }
        
        // Access log
        btnViewAccessLog.setOnClickListener {
            showAccessLog()
        }
        
        btnClearAccessLog.setOnClickListener {
            confirmClearAccessLog()
        }
        
        // Admin PIN
        btnSetAdminPin.setOnClickListener {
            showSetAdminPinDialog()
        }
        
        // User role
        spinnerUserRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val role = if (position == 0) SecurePreferences.ROLE_ADMIN else SecurePreferences.ROLE_CONTROLLED
                if (role != securePrefs.getUserRole()) {
                    securePrefs.setUserRole(role)
                    securePrefs.logAccess("Admin changed user role to $role")
                }
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun loadSettings() {
        // Access restrictions
        switchAccessRestriction.isChecked = securePrefs.isAccessRestricted()
        updateAccessTimeDisplay()
        updateAccessTimeVisibility()
        
        // Check-in
        switchCheckIn.isChecked = securePrefs.isCheckInRequired()
        etCheckInInterval.setText(securePrefs.getCheckInIntervalHours().toString())
        updateLastCheckInDisplay()
        updateCheckInVisibility()
        
        // Feature permissions
        switchCanAddImages.isChecked = securePrefs.canAddImages()
        switchCanDeleteImages.isChecked = securePrefs.canDeleteImages()
        switchCanEditImages.isChecked = securePrefs.canEditImages()
        switchCanShareImages.isChecked = securePrefs.canShareImages()
        switchCanChangePin.isChecked = securePrefs.canChangeUserPin()
        
        // User role
        val roleIndex = if (securePrefs.getUserRole() == SecurePreferences.ROLE_ADMIN) 0 else 1
        spinnerUserRole.setSelection(roleIndex)
        
        // Admin PIN button text
        btnSetAdminPin.text = if (securePrefs.hasAdminPin()) {
            getString(R.string.change_pin) + " (${getString(R.string.admin_pin)})"
        } else {
            getString(R.string.set_admin_pin)
        }
    }
    
    private fun updateAccessTimeVisibility() {
        val visible = switchAccessRestriction.isChecked
        btnSetAccessStartTime.visibility = if (visible) View.VISIBLE else View.GONE
        btnSetAccessEndTime.visibility = if (visible) View.VISIBLE else View.GONE
        tvAccessStartTime.visibility = if (visible) View.VISIBLE else View.GONE
        tvAccessEndTime.visibility = if (visible) View.VISIBLE else View.GONE
    }
    
    private fun updateCheckInVisibility() {
        val visible = switchCheckIn.isChecked
        etCheckInInterval.visibility = if (visible) View.VISIBLE else View.GONE
        tvLastCheckIn.visibility = if (visible) View.VISIBLE else View.GONE
        findViewById<TextView>(R.id.tvCheckInIntervalLabel)?.visibility = if (visible) View.VISIBLE else View.GONE
    }
    
    private fun updateAccessTimeDisplay() {
        val calendar = Calendar.getInstance()
        
        val startTime = securePrefs.getAccessStartTime()
        if (startTime > 0) {
            calendar.timeInMillis = startTime
            tvAccessStartTime.text = "${getString(R.string.access_start_time)}: ${timeFormat.format(calendar.time)}"
        } else {
            tvAccessStartTime.text = "${getString(R.string.access_start_time)}: ${getString(R.string.not_set)}"
        }
        
        val endTime = securePrefs.getAccessEndTime()
        if (endTime < Long.MAX_VALUE) {
            calendar.timeInMillis = endTime
            tvAccessEndTime.text = "${getString(R.string.access_end_time)}: ${timeFormat.format(calendar.time)}"
        } else {
            tvAccessEndTime.text = "${getString(R.string.access_end_time)}: ${getString(R.string.not_set)}"
        }
    }
    
    private fun updateLastCheckInDisplay() {
        val lastCheckIn = securePrefs.getLastCheckInTime()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        tvLastCheckIn.text = "${getString(R.string.last_check_in)}: ${dateFormat.format(Date(lastCheckIn))}"
    }
    
    private fun showTimePicker(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()
        val time = if (isStartTime) securePrefs.getAccessStartTime() else securePrefs.getAccessEndTime()
        if (time > 0 && time < Long.MAX_VALUE) {
            calendar.timeInMillis = time
        }
        
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                
                if (isStartTime) {
                    securePrefs.setAccessStartTime(calendar.timeInMillis)
                    securePrefs.logAccess("Admin set access start time to ${timeFormat.format(calendar.time)}")
                } else {
                    securePrefs.setAccessEndTime(calendar.timeInMillis)
                    securePrefs.logAccess("Admin set access end time to ${timeFormat.format(calendar.time)}")
                }
                updateAccessTimeDisplay()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
    
    private fun showSetAdminPinDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_set_admin_pin, null)
        val currentPinInput = dialogView.findViewById<TextInputEditText>(R.id.currentAdminPinInput)
        val newPinInput = dialogView.findViewById<TextInputEditText>(R.id.newAdminPinInput)
        val confirmPinInput = dialogView.findViewById<TextInputEditText>(R.id.confirmAdminPinInput)
        
        // If admin PIN already exists, show current PIN field
        if (securePrefs.hasAdminPin()) {
            currentPinInput.visibility = View.VISIBLE
        } else {
            currentPinInput.visibility = View.GONE
        }
        
        AlertDialog.Builder(this)
            .setTitle(if (securePrefs.hasAdminPin()) getString(R.string.change_pin) else getString(R.string.set_admin_pin))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val currentPin = currentPinInput.text?.toString() ?: ""
                val newPin = newPinInput.text?.toString() ?: ""
                val confirmPin = confirmPinInput.text?.toString() ?: ""
                
                when {
                    securePrefs.hasAdminPin() && currentPin != securePrefs.getAdminPin() -> {
                        Toast.makeText(this, getString(R.string.wrong_admin_pin), Toast.LENGTH_SHORT).show()
                    }
                    newPin.length < 4 -> {
                        Toast.makeText(this, getString(R.string.pin_min_length), Toast.LENGTH_SHORT).show()
                    }
                    newPin != confirmPin -> {
                        Toast.makeText(this, getString(R.string.pin_mismatch), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        securePrefs.saveAdminPin(newPin)
                        Toast.makeText(this, getString(R.string.admin_pin_set), Toast.LENGTH_SHORT).show()
                        securePrefs.logAccess("Admin PIN was set/changed")
                        btnSetAdminPin.text = getString(R.string.change_pin) + " (${getString(R.string.admin_pin)})"
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun showAccessLog() {
        val log = securePrefs.getAccessLog()
        val formattedLog = if (log.isEmpty()) {
            getString(R.string.no_access_log)
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            log.split("\n").filter { it.isNotBlank() }.joinToString("\n") { entry ->
                val parts = entry.split(":", limit = 2)
                if (parts.size == 2) {
                    val timestamp = parts[0].toLongOrNull()
                    val action = parts[1]
                    if (timestamp != null) {
                        "${dateFormat.format(Date(timestamp))} - $action"
                    } else {
                        entry
                    }
                } else {
                    entry
                }
            }
        }
        
        val scrollView = ScrollView(this)
        val textView = TextView(this).apply {
            text = formattedLog
            setPadding(24, 24, 24, 24)
            textSize = 12f
        }
        scrollView.addView(textView)
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.access_log))
            .setView(scrollView)
            .setPositiveButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun confirmClearAccessLog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.clear_access_log))
            .setMessage("Are you sure you want to clear the access log?")
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                securePrefs.clearAccessLog()
                Toast.makeText(this, getString(R.string.log_cleared), Toast.LENGTH_SHORT).show()
                securePrefs.logAccess("Admin cleared access log")
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
}
