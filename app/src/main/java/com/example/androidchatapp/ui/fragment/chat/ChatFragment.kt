package com.example.androidchatapp.ui.fragment.chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.ChatActivity
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.ui.ChatAdapter
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
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val chatPartner = (activity as ChatActivity).chatPartner

        Log.i(TAG, currentUserId.toString())
        Log.i(TAG, chatPartner.uid.toString())
        Log.i(TAG, "listening for messages")

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/${currentUserId}/${chatPartner.uid}")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "new child added")
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

        val chatPartner = (activity as ChatActivity).chatPartner

        val message = txtChatInput.editText?.text.toString()
        val fromId = FirebaseAuth.getInstance().currentUser?.uid
        val ownRef = FirebaseDatabase.getInstance().getReference("/user-messages/${fromId}/${chatPartner.uid}").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/${chatPartner.uid}/${fromId}").push()
        val ownLatestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/${fromId}/${chatPartner.uid}")
        val toLatestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/${chatPartner.uid}/${fromId}")

        val chatMessage = ChatMessage(
            ownRef.key!!,
            message,
            fromId!!,
            (activity as ChatActivity).chatPartner.uid!!,
            System.currentTimeMillis()/1000)

        ownRef.setValue(chatMessage).addOnCompleteListener {
            Log.i(TAG, "Saved chat message")
        }.addOnFailureListener {
            TODO("TODO IMPLEMENT")
        }

        toRef.setValue(chatMessage)
        ownLatestMessageRef.setValue(chatMessage)
        toLatestMessageRef.setValue(chatMessage)


    }



}