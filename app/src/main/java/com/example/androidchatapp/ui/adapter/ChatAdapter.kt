package com.example.androidchatapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.databinding.ItemChatReceivedBinding
import com.example.androidchatapp.databinding.ItemChatSentBinding
import com.example.androidchatapp.helper.DateParser
import com.example.androidchatapp.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*

const val VIEW_TYPE_MESSAGE_SENT = 1
const val VIEW_TYPE_MESSAGE_RECEIVED = 2

class ChatAdapter (
    private val chatMessages: List<ChatMessage>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemChatSentBinding.bind(itemView)

        fun dataBind(message: ChatMessage) {
            binding.txtContent.text = message.content
            binding.txtTimestampSent.text =
                DateParser.parseTimestamp(message.timestamp.toString(), false)
        }
    }

    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemChatReceivedBinding.bind(itemView)

        fun dataBind(message: ChatMessage) {
            binding.txtContent.text = message.content
            binding.txtTimestampReceived.text =
                DateParser.parseTimestamp(message.timestamp.toString(), false)
        }
    }


    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = chatMessages[position]

        return if(message.toId == FirebaseAuth.getInstance().currentUser?.uid) {
            VIEW_TYPE_MESSAGE_RECEIVED
        } else {
            VIEW_TYPE_MESSAGE_SENT

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_sent, parent, false)
                return SentMessageHolder(view)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_received, parent, false)
                return ReceivedMessageHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_sent, parent, false)
                return SentMessageHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMessages[position]

        when(holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                (holder as SentMessageHolder).dataBind(message)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                (holder as ReceivedMessageHolder).dataBind(message)
            }
        }
    }
}
