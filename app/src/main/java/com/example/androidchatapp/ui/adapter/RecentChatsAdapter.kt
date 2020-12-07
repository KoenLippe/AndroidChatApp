package com.example.androidchatapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatListItem
import com.example.androidchatapp.model.ChatMessage
import kotlinx.android.synthetic.main.item_chat_list_item.view.*

class RecentChatsAdapter(
    private val chats: List<ChatMessage>
): RecyclerView.Adapter<RecentChatsAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun dataBind(chat: ChatMessage) {
            Log.d("ADAPTER", chat.toString())
            itemView.txtName.text = chat.fromId
            itemView.txtLatestMessage.text = chat.content
            itemView.txtTimestamp.text = chat.timestamp.toString()

            //TODO call database with chatpartner UserID do get user info
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(chats[position])
    }


}