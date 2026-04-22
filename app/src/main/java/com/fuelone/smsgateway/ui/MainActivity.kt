package com.fuelone.smsgateway.ui

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fuelone.smsgateway.R
import com.fuelone.smsgateway.data.AppPreferences
import com.fuelone.smsgateway.data.GatewayDatabase
import com.fuelone.smsgateway.databinding.ActivityMainBinding
import com.fuelone.smsgateway.service.SmsGatewayService
import com.fuelone.smsgateway.util.NetworkUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private val REQUIRED_PERMISSIONS = buildList {
            add(Manifest.permission.SEND_SMS)
            add(Manifest.permission.RECEIVE_SMS)
            add(Manifest.permission.READ_SMS)
            add(Manifest.permission.READ_PHONE_STATE)
            add(Manifest.permission.READ_PHONE_NUMBERS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }.toTypedArray()
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: AppPreferences
    private lateinit var db: GatewayDatabase

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val allGranted = results.values.all { it }
        if (allGranted) {
            Toast.makeText(this, "All permissions granted ✓", Toast.LENGTH_SHORT).show()
        } else {
            val denied = results.filterValues { !it }.keys.joinToString(", ")
            Toast.makeText(this, "Denied: $denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        prefs = AppPreferences(this)
        db    = GatewayDatabase.getInstance(this)

        setupUI()
        checkPermissions()
        startStatusPolling()
        requestBatteryOptimizationExemption()
    }

    private fun setupUI() {
        // Gateway toggle
        binding.switchGateway.isChecked = prefs.gatewayEnabled
        binding.switchGateway.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) startGateway() else stopGateway()
        }

        // Open Settings
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Open Inbox
        binding.btnInbox.setOnClickListener {
            startActivity(Intent(this, InboxActivity::class.java))
        }

        // Connection mode chip selection
        binding.chipWifi.setOnClickListener      { prefs.connectionMode = com.fuelone.smsgateway.model.ConnectionMode.WIFI;      refreshModeDisplay() }
        binding.chipUsb.setOnClickListener       { prefs.connectionMode = com.fuelone.smsgateway.model.ConnectionMode.USB;       refreshModeDisplay() }
        binding.chipBluetooth.setOnClickListener { prefs.connectionMode = com.fuelone.smsgateway.model.ConnectionMode.BLUETOOTH; refreshModeDisplay() }

        // Copy IP/port to clipboard
        binding.tvEndpoint.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("endpoint", binding.tvEndpoint.text))
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        refreshModeDisplay()
        updateStats()
    }

    private fun startGateway() {
        if (!hasRequiredPermissions()) {
            binding.switchGateway.isChecked = false
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
            return
        }
        prefs.gatewayEnabled = true
        val intent = Intent(this, SmsGatewayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Gateway started ▶", Toast.LENGTH_SHORT).show()
        updateStatusBadge(true)
    }

    private fun stopGateway() {
        prefs.gatewayEnabled = false
        stopService(Intent(this, SmsGatewayService::class.java))
        Toast.makeText(this, "Gateway stopped ■", Toast.LENGTH_SHORT).show()
        updateStatusBadge(false)
    }

    private fun updateStatusBadge(running: Boolean) {
        binding.tvStatus.text = if (running) "● ACTIVE" else "○ STOPPED"
        binding.tvStatus.setTextColor(
            ContextCompat.getColor(this, if (running) android.R.color.holo_green_dark else android.R.color.holo_red_dark)
        )
    }

    private fun refreshModeDisplay() {
        val mode = prefs.connectionMode
        binding.chipWifi.isChecked      = mode == com.fuelone.smsgateway.model.ConnectionMode.WIFI
        binding.chipUsb.isChecked       = mode == com.fuelone.smsgateway.model.ConnectionMode.USB
        binding.chipBluetooth.isChecked = mode == com.fuelone.smsgateway.model.ConnectionMode.BLUETOOTH

        val ip = getLocalIpAddress()
        val endpoint = when (mode) {
            com.fuelone.smsgateway.model.ConnectionMode.WIFI      -> "http://$ip:${prefs.port}"
            com.fuelone.smsgateway.model.ConnectionMode.USB       -> "http://localhost:${prefs.port}  (adb forward tcp:${prefs.port} tcp:${prefs.port})"
            com.fuelone.smsgateway.model.ConnectionMode.BLUETOOTH -> "BT RFCOMM — UUID: ${com.fuelone.smsgateway.bluetooth.BluetoothService.BT_UUID}"
        }
        binding.tvEndpoint.text = endpoint
    }

    private fun updateStats() {
        lifecycleScope.launch {
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }
            val sentToday  = db.messageDao().countSentToday(cal.timeInMillis)
            val unread     = db.messageDao().getUnreadInbox().size
            val totalInbox = db.messageDao().getInbox().size

            binding.tvSentToday.text  = sentToday.toString()
            binding.tvUnread.text     = unread.toString()
            binding.tvTotalInbox.text = totalInbox.toString()
            binding.tvMode.text       = prefs.connectionMode.name
            binding.tvSimNumber.text  = prefs.simNumber
        }
    }

    private fun startStatusPolling() {
        lifecycleScope.launch {
            while (isActive) {
                updateStats()
                updateStatusBadge(prefs.gatewayEnabled)
                refreshModeDisplay()
                delay(5_000)
            }
        }
    }

    private fun hasRequiredPermissions() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions() {
        val missing = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) permissionLauncher.launch(missing.toTypedArray())
    }

    private fun requestBatteryOptimizationExemption() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            try {
                startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:$packageName")
                })
            } catch (e: Exception) {
                Log.w(TAG, "Could not request battery optimization exemption")
            }
        }
    }

    private fun getLocalIpAddress(): String = try {
        NetworkInterface.getNetworkInterfaces().toList()
            .flatMap { it.inetAddresses.toList() }
            .firstOrNull { !it.isLoopbackAddress && it.hostAddress?.contains('.') == true }
            ?.hostAddress ?: "0.0.0.0"
    } catch (e: Exception) { "0.0.0.0" }

    override fun onResume() {
        super.onResume()
        updateStats()
        refreshModeDisplay()
        binding.switchGateway.isChecked = prefs.gatewayEnabled
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> { startActivity(Intent(this, SettingsActivity::class.java)); true }
            R.id.action_inbox    -> { startActivity(Intent(this, InboxActivity::class.java)); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
