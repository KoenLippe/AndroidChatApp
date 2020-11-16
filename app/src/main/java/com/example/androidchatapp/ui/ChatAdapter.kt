package com.example.androidchatapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatListItem
import kotlinx.android.synthetic.main.item_chat_list_item.view.*

class ChatAdapter(
    private val chats: List<ChatListItem>
//    private val onChat:(ColorItem) -> Unit
): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun dataBind(chat: ChatListItem) {
            itemView.txtName.text = chat.name
            itemView.txtLatestMessage.text = chat.latestMessage
            itemView.txtTimestamp.text = chat.timestamp
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