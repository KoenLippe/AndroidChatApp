package com.example.androidchatapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatListItem
import com.example.androidchatapp.ui.ChatAdapter
import kotlinx.android.synthetic.main.fragment_chat_overview.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatOverviewFragment : Fragment() {

    private val chats = arrayListOf<ChatListItem>()
    private val chatAdapter = ChatAdapter(chats)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_overview, container, false)
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