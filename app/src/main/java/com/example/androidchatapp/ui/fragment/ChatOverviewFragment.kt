package com.example.androidchatapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatListItem
import com.example.androidchatapp.model.User
import com.example.androidchatapp.ui.ChatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chat_overview.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatOverviewFragment : Fragment() {

    companion object {
        const val TAG = "ChatOverviewFragment"
    }

    private val chats = arrayListOf<ChatListItem>()
    private val chatAdapter = ChatAdapter(chats)
    //TODO Add viemodel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_overview, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_chat_overview, menu) // TODO is not working yet.
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (x in 0..5) {
            chats.add(ChatListItem("Tom Vuyst", "asdf", "Yooooo!", "09:45"))
        }

        initRv()
    }

    private fun initRv() {
        rvChats.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        }
    }
}