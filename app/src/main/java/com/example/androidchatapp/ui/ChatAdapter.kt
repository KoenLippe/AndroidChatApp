package com.example.androidchatapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.databinding.ItemChatReceivedBinding
import com.example.androidchatapp.databinding.ItemChatSentBinding
import com.example.androidchatapp.model.ChatMessage

const val VIEW_TYPE_MESSAGE_SENT = 1
const val VIEW_TYPE_MESSAGE_RECEIVED = 2

class ChatAdapter (
    private val chatMessages: List<ChatMessage>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemChatSentBinding.bind(itemView)

        fun dataBind(message: ChatMessage) {
            binding.txtUser.text = message.sender
            binding.txtContent.text = message.content
        }
    }

    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemChatReceivedBinding.bind(itemView)

        fun dataBind(message: ChatMessage) {
            binding.txtUser.text = message.sender
            binding.txtContent.text = message.content
        }
    }


    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = chatMessages[position]

        return if(message.sender == "1") {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
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
                //TODO Replace this
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_received, parent, false)
                return ReceivedMessageHolder(view)
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
