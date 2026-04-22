package com.fuelone.smsgateway.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fuelone.smsgateway.R
import com.fuelone.smsgateway.data.AppPreferences
import com.fuelone.smsgateway.databinding.ActivitySettingsBinding
import com.fuelone.smsgateway.model.AutoReplyRule
import com.fuelone.smsgateway.model.ConnectionMode
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: AppPreferences
    private lateinit var rulesAdapter: AutoReplyRulesAdapter
    private val rulesList = mutableListOf<AutoReplyRule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Gateway Settings"

        prefs = AppPreferences(this)
        loadSettings()
        setupAutoReplyRules()
        setupListeners()
    }

    private fun loadSettings() {
        // Connection
        when (prefs.connectionMode) {
            ConnectionMode.WIFI      -> binding.radioWifi.isChecked = true
            ConnectionMode.USB       -> binding.radioUsb.isChecked  = true
            ConnectionMode.BLUETOOTH -> binding.radioBluetooth.isChecked = true
        }

        // Auth
        binding.etSecretKey.setText(prefs.secretKey)
        binding.etPort.setText(prefs.port.toString())

        // Supabase
        binding.etSupabaseUrl.setText(prefs.supabaseUrl)
        binding.etSupabaseKey.setText(prefs.supabaseAnonKey)

        // Webhook
        binding.etWebhookUrl.setText(prefs.webhookUrl)

        // General
        binding.switchAutoStart.isChecked = prefs.autoStart
        binding.tvSimNumber.text = detectSimNumber()
        prefs.simNumber = binding.tvSimNumber.text.toString()

        // Rules
        rulesList.clear()
        rulesList.addAll(prefs.autoReplyRules)
    }

    @SuppressLint("MissingPermission")
    private fun detectSimNumber(): String {
        return try {
            val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val line = tm.line1Number
            if (!line.isNullOrBlank()) line else "Not available"
        } catch (e: Exception) {
            "Permission required"
        }
    }

    private fun setupAutoReplyRules() {
        rulesAdapter = AutoReplyRulesAdapter(rulesList) { pos ->
            rulesList.removeAt(pos)
            rulesAdapter.notifyItemRemoved(pos)
        }
        binding.rvAutoReplyRules.apply {
            layoutManager = LinearLayoutManager(this@SettingsActivity)
            adapter = rulesAdapter
        }

        binding.btnAddRule.setOnClickListener { showAddRuleDialog() }
    }

    private fun showAddRuleDialog(existing: AutoReplyRule? = null, position: Int = -1) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_rule, null)
        val etKeyword  = dialogView.findViewById<TextInputEditText>(R.id.etKeyword)
        val etReply    = dialogView.findViewById<TextInputEditText>(R.id.etReplyMessage)

        existing?.let {
            etKeyword.setText(it.keyword)
            etReply.setText(it.reply)
        }

        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle(if (existing == null) "Add Auto-Reply Rule" else "Edit Rule")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val keyword = etKeyword.text?.toString()?.trim() ?: ""
                val reply   = etReply.text?.toString()?.trim() ?: ""
                if (keyword.isBlank() || reply.isBlank()) {
                    Toast.makeText(this, "Keyword and reply are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val rule = AutoReplyRule(keyword, reply)
                if (existing != null && position >= 0) {
                    rulesList[position] = rule
                    rulesAdapter.notifyItemChanged(position)
                } else {
                    rulesList.add(rule)
                    rulesAdapter.notifyItemInserted(rulesList.lastIndex)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener { saveSettings() }
        binding.btnTestConnection.setOnClickListener { testEndpoint() }
    }

    private fun saveSettings() {
        // Connection mode
        prefs.connectionMode = when {
            binding.radioWifi.isChecked      -> ConnectionMode.WIFI
            binding.radioUsb.isChecked       -> ConnectionMode.USB
            binding.radioBluetooth.isChecked -> ConnectionMode.BLUETOOTH
            else -> ConnectionMode.WIFI
        }

        // Validate port
        val portStr = binding.etPort.text?.toString() ?: ""
        val port    = portStr.toIntOrNull()
        if (port == null || port !in 1024..65535) {
            binding.etPort.error = "Port must be 1024–65535"
            return
        }

        prefs.secretKey     = binding.etSecretKey.text?.toString()?.trim() ?: ""
        prefs.port          = port
        prefs.supabaseUrl   = binding.etSupabaseUrl.text?.toString()?.trim() ?: AppPreferences.DEFAULT_SUPABASE_URL
        prefs.supabaseAnonKey = binding.etSupabaseKey.text?.toString()?.trim() ?: ""
        prefs.webhookUrl    = binding.etWebhookUrl.text?.toString()?.trim() ?: ""
        prefs.autoStart     = binding.switchAutoStart.isChecked
        prefs.autoReplyRules = rulesList.toList()

        Snackbar.make(binding.root, "Settings saved ✓", Snackbar.LENGTH_SHORT).show()
    }

    private fun testEndpoint() {
        val mode = when {
            binding.radioWifi.isChecked -> "WiFi"
            binding.radioUsb.isChecked  -> "USB"
            else -> "Bluetooth"
        }
        Toast.makeText(this, "Mode: $mode — restart gateway to apply changes", Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) { finish(); return true }
        return super.onOptionsItemSelected(item)
    }
}

// ─── Auto-Reply Rules RecyclerView Adapter ────────────────────────────────────
class AutoReplyRulesAdapter(
    private val rules: MutableList<AutoReplyRule>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<AutoReplyRulesAdapter.ViewHolder>() {

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvKeyword: TextView  = view.findViewById(R.id.tvKeyword)
        val tvReply: TextView    = view.findViewById(R.id.tvReply)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_auto_reply_rule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rule = rules[position]
        holder.tvKeyword.text = "\"${rule.keyword}\""
        holder.tvReply.text   = rule.reply
        holder.btnDelete.setOnClickListener { onDelete(holder.adapterPosition) }
    }

    override fun getItemCount() = rules.size
}
