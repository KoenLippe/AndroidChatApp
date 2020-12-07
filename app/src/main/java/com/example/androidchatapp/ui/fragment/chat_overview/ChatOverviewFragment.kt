package com.example.androidchatapp.ui.fragment.chat_overview

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.ChatActivity
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatListItem
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.ui.adapter.RecentChatsAdapter
import com.example.androidchatapp.ui.fragment.chat.ChatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat_overview.*
import java.util.*

class ChatOverviewFragment : Fragment() {

    companion object {
        const val TAG = "ChatOverviewFragment"
    }

    private val chats = arrayListOf<ChatMessage>()
    private val chatAdapter = RecentChatsAdapter(chats)
    //TODO Add viemodel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLatestMessages()
        initRv()
    }

    private fun initRv() {
        rvChats.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        }
    }

    private fun getLatestMessages() {
        //TODO move to view model
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val latestMessageRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/${currentUserId}")

        latestMessageRef.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d("CHATOVERVIEW", chatMessage.toString())

                if(chatMessage != null) {
                    chats.add(chatMessage)
                    chatAdapter.notifyDataSetChanged()
                    rvChats.scrollToPosition(chatAdapter.itemCount - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("CHATOVERVIEW", "on child changed")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("CHATOVERVIEW", "onChildRemoved")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })




    }
}