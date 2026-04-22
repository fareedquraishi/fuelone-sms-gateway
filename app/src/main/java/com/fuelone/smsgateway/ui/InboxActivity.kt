package com.fuelone.smsgateway.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fuelone.smsgateway.R
import com.fuelone.smsgateway.data.GatewayDatabase
import com.fuelone.smsgateway.data.MessageEntity
import com.fuelone.smsgateway.databinding.ActivityInboxBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding
    private lateinit var db: GatewayDatabase
    private lateinit var adapter: InboxAdapter
    private val messages = mutableListOf<MessageEntity>()
    private val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Inbox"

        db = GatewayDatabase.getInstance(this)

        adapter = InboxAdapter(messages, sdf)
        binding.rvInbox.layoutManager = LinearLayoutManager(this)
        binding.rvInbox.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener { loadMessages() }

        loadMessages()
        observeInbox()
    }

    private fun loadMessages() {
        lifecycleScope.launch {
            val result = db.messageDao().getInbox()
            messages.clear()
            messages.addAll(result)
            adapter.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
            binding.tvEmpty.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE

            // Mark all as read
            result.filter { it.readAt == null }.forEach { msg ->
                db.messageDao().markRead(msg.id)
            }
        }
    }

    private fun observeInbox() {
        lifecycleScope.launch {
            db.messageDao().observeInbox().collect { result ->
                messages.clear()
                messages.addAll(result)
                adapter.notifyDataSetChanged()
                binding.tvEmpty.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) { finish(); return true }
        return super.onOptionsItemSelected(item)
    }
}

// ─── Inbox RecyclerView Adapter ───────────────────────────────────────────────
class InboxAdapter(
    private val messages: List<MessageEntity>,
    private val sdf: SimpleDateFormat
) : RecyclerView.Adapter<InboxAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFrom: TextView        = view.findViewById(R.id.tvFrom)
        val tvMessage: TextView     = view.findViewById(R.id.tvMessage)
        val tvTime: TextView        = view.findViewById(R.id.tvTime)
        val tvKeyword: TextView     = view.findViewById(R.id.tvKeyword)
        val tvAutoReply: TextView   = view.findViewById(R.id.tvAutoReply)
        val tvUnread: TextView      = view.findViewById(R.id.tvUnreadBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inbox_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = messages[position]
        holder.tvFrom.text    = msg.phone
        holder.tvMessage.text = msg.message
        holder.tvTime.text    = sdf.format(Date(msg.createdAt))

        holder.tvKeyword.visibility = if (msg.keywordMatched != null) View.VISIBLE else View.GONE
        holder.tvKeyword.text       = "Keyword: ${msg.keywordMatched}"

        holder.tvAutoReply.visibility = if (msg.autoReplySent) View.VISIBLE else View.GONE
        holder.tvAutoReply.text       = "✓ Auto-reply sent"

        holder.tvUnread.visibility = if (msg.readAt == null) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = messages.size
}
