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
import com.example.androidchatapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_chat_list_item.view.*

class RecentChatsAdapter(
    private val chats: List<ChatMessage>
): RecyclerView.Adapter<RecentChatsAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun dataBind(chat: ChatMessage) {
            itemView.txtLatestMessage.text = chat.content
//            itemView.txtTimestamp.text = chat.timestamp.toString()
            // TODO Parse timestamp

            // Get user info
            val ref = FirebaseDatabase.getInstance().getReference("/users/${chat.fromId}")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    itemView.txtName.text = user?.username
                }

                override fun onCancelled(error: DatabaseError) {
                    // No action required
                    return
                }
            })
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