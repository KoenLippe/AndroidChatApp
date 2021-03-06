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
import com.example.androidchatapp.vm.LatestMessagesViewModel
import kotlinx.android.synthetic.main.fragment_chat_overview.*
import androidx.lifecycle.Observer
import com.example.androidchatapp.ChatOverviewActivity
import com.example.androidchatapp.model.User

class ChatOverviewFragment : Fragment() {

    companion object {
        const val TAG = "FRAGMENT_CHAT_OVERVIEW"
    }

    private val latestMessagesViewModel: LatestMessagesViewModel by viewModels()

    private val latestChats = arrayListOf<ChatMessage>()
    private val latestChatAdapter = LatestChatsAdapter(latestChats, ::onChatClick)

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

        latestMessagesViewModel.fetching.observe(viewLifecycleOwner, Observer {
            if(it) {
                pbLatestMessages.visibility = View.VISIBLE
            } else {
                pbLatestMessages.visibility = View.INVISIBLE
            }
        })

        latestMessagesViewModel.latestMessages.observe(viewLifecycleOwner, Observer { it ->
            if(it.isEmpty()) {
                txtNoMessages.visibility = View.VISIBLE
                return@Observer
            }

            latestChats.clear()
            latestChats.addAll(it)
            latestChats.sortBy { it.timestamp }
            latestChats.reverse()
            latestChatAdapter.notifyDataSetChanged()

            // Scroll to top
            rvChats.scrollToPosition(0)
            Log.i(TAG, "Latest messages updated")

            txtNoMessages.visibility = View.INVISIBLE
        })

        latestMessagesViewModel.getLatestMessages()
    }

    private fun onChatClick(user: User) {
        //Start chat activity
        (activity as ChatOverviewActivity).startChatActivity(user)
    }

    private fun initRv() {
        rvChats.apply {
            adapter = latestChatAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        }
    }
}