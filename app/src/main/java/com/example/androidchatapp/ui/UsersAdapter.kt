package com.example.androidchatapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.User
import kotlinx.android.synthetic.main.item_chat_list_item.view.*

class UsersAdapter(
    private val users: List<User>
): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun dataBind(user: User) {
            itemView.txtName.text = user.uid
            itemView.txtLatestMessage.text = user.username
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        // TODO Replace with own styling
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(users[position])
    }


}