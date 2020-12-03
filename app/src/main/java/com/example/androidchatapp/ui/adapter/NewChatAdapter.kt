package com.example.androidchatapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.User
import kotlinx.android.synthetic.main.item_chat_list_item.view.*

class NewChatAdapter(
    private val users: List<User>,
    private val onNewChatClick:(User) -> Unit
): RecyclerView.Adapter<NewChatAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onNewChatClick(users[adapterPosition])
            }
        }

        // TODO add tactile click animation when clicked

        fun dataBind(user: User) {
            itemView.txtName.text = user.username
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_new_chat_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(users[position])
    }


}