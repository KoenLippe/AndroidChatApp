package com.example.androidchatapp.ui.fragment.chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.ChatActivity
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.model.User
import com.example.androidchatapp.vm.ChatViewModel
import androidx.lifecycle.Observer
import com.example.androidchatapp.ui.adapter.ChatAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatFragment : Fragment() {

    companion object {
        private const val TAG = "FRAGMENT_CHAT"
    }

    private val chatViewModel: ChatViewModel by viewModels()

    private val chatMessages = arrayListOf<ChatMessage>()
    private val chatMessagesAdapter = ChatAdapter(chatMessages)

    private lateinit var chatPartner: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatPartner = (activity as ChatActivity).chatPartner

        initRv()

        btnSend.setOnClickListener {
            val message = txtChatInput.editText?.text
            chatViewModel.sendMessage(chatPartner, message.toString())
            message?.clear()
        }

        chatViewModel.messages.observe(viewLifecycleOwner, Observer {
            chatMessages.clear()
            chatMessages.addAll(it)
            chatMessagesAdapter.notifyDataSetChanged()

            // Scroll to bottom
            rvChatLog.scrollToPosition(chatMessagesAdapter.itemCount - 1)
            Log.i(TAG, "Chat messages added")
        })

        chatViewModel.listenForMessages(chatPartner)
    }

    private fun initRv() {
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        manager.stackFromEnd = true

        rvChatLog.apply {
            adapter = chatMessagesAdapter
            layoutManager = manager
        }
    }
}