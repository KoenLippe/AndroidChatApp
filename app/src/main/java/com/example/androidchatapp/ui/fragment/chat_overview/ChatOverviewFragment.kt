package com.example.androidchatapp.ui.fragment.chat_overview

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.R
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.ui.adapter.LatestChatsAdapter
import com.example.androidchatapp.ui.vm.LatestMessagesViewModel
import kotlinx.android.synthetic.main.fragment_chat_overview.*
import androidx.lifecycle.Observer

class ChatOverviewFragment : Fragment() {

    companion object {
        const val TAG = "ChatOverviewFragment"
    }

    private val latestMessagesViewModel: LatestMessagesViewModel by viewModels()

    private val latestChats = arrayListOf<ChatMessage>()
    private val latestChatAdapter = LatestChatsAdapter(latestChats)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()

        latestMessagesViewModel.latestMessages.observe(viewLifecycleOwner, Observer {
            latestChats.clear()
            latestChats.addAll(it)
            latestChatAdapter.notifyDataSetChanged()

            // Scroll to bottom
            rvChats.scrollToPosition(latestChatAdapter.itemCount - 1)
            Log.i(TAG, "Latest messages updated")
        })


        // Prevent list doubling
        latestMessagesViewModel.getLatestMessages()

        // Todo add swipe down to refresh
    }

    private fun initRv() {
        rvChats.apply {
            adapter = latestChatAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        }
    }
}