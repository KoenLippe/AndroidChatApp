package com.example.androidchatapp.ui.fragment.chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.ChatActivity
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatListItem
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.ui.ChatAdapter
import com.example.androidchatapp.ui.adapter.RecentChatsAdapter
import com.example.androidchatapp.ui.fragment.chat_overview.NewMessageFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat_overview.*
import java.time.Instant.now
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatFragment : Fragment() {

    companion object {
        private const val TAG = "CHAT_FRAGMENT"
    }

    private val chatMessages = arrayListOf<ChatMessage>()
    private val chatMessagesAdapter = ChatAdapter(chatMessages)

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


        listenForMessages()
        btnSend.setOnClickListener { sendMessage() }

        // Setup dummy data
        for (x in 0..5) {
            if(x % 2 == 0) {
                chatMessages.add(ChatMessage(Random(1).nextInt().toString(),"Hi! How are you?", "1", "0101", 123123123))
            } else {
                chatMessages.add(ChatMessage(Random(2).nextInt().toString(),"Good!", "2", "0101", 123123))
            }
        }

        chatMessagesAdapter.notifyDataSetChanged()

        initRv()
    }

    private fun initRv() {
        var manager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        manager.stackFromEnd = true

        rvChatLog.apply {
            adapter = chatMessagesAdapter
            layoutManager = manager
        }
    }


    private fun listenForMessages() {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {
                    Log.i(TAG, chatMessage.content)
                    if(chatMessage.fromId == userUid || chatMessage.toId == userUid) {
                        chatMessages.add(chatMessage)
                        chatMessagesAdapter.notifyDataSetChanged()
                        rvChatLog.scrollToPosition(chatMessagesAdapter.itemCount - 1)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage() {
        Log.i(TAG, "on send")
        // TODO extract logic to viewModel

        val message = txtChatInput.editText?.text.toString()
        val fromId = FirebaseAuth.getInstance().currentUser?.uid
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage = ChatMessage(
            reference.key!!,
            message,
            fromId!!,
            (activity as ChatActivity).chatPartner.uid!!,
            System.currentTimeMillis()/1000)
        reference.setValue(chatMessage).addOnCompleteListener {
            Log.i(TAG, "Saved chat message")
        }.addOnFailureListener {
            TODO("TODO IMPLEMENT")
        }
    }
}