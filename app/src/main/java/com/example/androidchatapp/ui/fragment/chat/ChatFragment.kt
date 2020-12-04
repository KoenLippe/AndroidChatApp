package com.example.androidchatapp.ui.fragment.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatListItem
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.ui.ChatAdapter
import com.example.androidchatapp.ui.adapter.RecentChatsAdapter
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat_overview.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatFragment : Fragment() {

    private val chatMessages = arrayListOf<ChatMessage>()
    private val chatMessagesAdapter = ChatAdapter(chatMessages)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        for (x in 0..5) {
            if(x % 2 == 0) {
                chatMessages.add(ChatMessage("Sent", "1", "0101"))
            } else {
                chatMessages.add(ChatMessage("Received", "2", "0101"))
            }
        }

        chatMessagesAdapter.notifyDataSetChanged()

        initRv()
    }

    private fun initRv() {
        rvChatLog.apply {
            adapter = chatMessagesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        }
    }
}