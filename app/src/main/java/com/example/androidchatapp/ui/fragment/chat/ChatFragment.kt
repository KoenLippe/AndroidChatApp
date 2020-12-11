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
import com.example.androidchatapp.ui.ChatAdapter
import com.example.androidchatapp.ui.vm.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatFragment : Fragment() {

    companion object {
        private const val TAG = "CHAT_FRAGMENT"
    }

    private val chatViewModel: ChatViewModel by viewModels()

    private val chatMessages = arrayListOf<ChatMessage>()
    private val chatMessagesAdapter = ChatAdapter(chatMessages)

    private lateinit var chatPartner: User;

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

        listenForMessages()

        btnSend.setOnClickListener {
            val message: String = txtChatInput.editText?.text.toString()
            chatViewModel.sendMessage(chatPartner, message)
        }

        initRv()
    }

    private fun initRv() {
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        manager.stackFromEnd = true

        rvChatLog.apply {
            adapter = chatMessagesAdapter
            layoutManager = manager
        }
    }


    private fun listenForMessages() {
        // TODO: Put into view model
        Log.i(TAG, "Listening for messages")

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val chatPartner = (activity as ChatActivity).chatPartner
        val ref = FirebaseDatabase.getInstance()
            .getReference("/user-messages/${currentUserId}/${chatPartner.uid}")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {
                    Log.i(TAG, chatMessage.content)
                    if(chatMessage.fromId == currentUserId || chatMessage.toId == currentUserId) {
                        chatMessages.add(chatMessage)
                        chatMessagesAdapter.notifyDataSetChanged()
                        rvChatLog.scrollToPosition(chatMessagesAdapter.itemCount - 1)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // No action required
                return
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {
                    chatMessages.remove(chatMessage)
                    chatMessagesAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // No action required
                return
            }

            override fun onCancelled(error: DatabaseError) {
                // No action required
                return
            }

        })
    }


}