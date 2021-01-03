package com.example.androidchatapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.helper.DateParser
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_chat_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val MESSAGE_TRUNCATE_VALUE = 25

class LatestChatsAdapter(
    private val chats: List<ChatMessage>,
    private val onChatClick: (User) -> Unit
): RecyclerView.Adapter<LatestChatsAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var user: User

        init {
            itemView.setOnClickListener { onChatClick(user) }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun dataBind(chat: ChatMessage) {
            itemView.txtLatestMessage.text = chat.content.take(MESSAGE_TRUNCATE_VALUE)
            itemView.txtTimestamp.text = DateParser.parseTimestamp(chat.timestamp.toString(), true)

            // Get user info
            val chatPartnerId = if(chat.fromId == FirebaseAuth.getInstance().uid)
                chat.toId else chat.fromId
            val ref = FirebaseDatabase.getInstance().getReference("/users/${chatPartnerId}")

            GlobalScope.launch(Dispatchers.IO) {
                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user = snapshot.getValue(User::class.java)!!
                        itemView.txtName.text = user.username
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // No action required
                        return
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_list_item, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(chats[position])
    }
}