package com.example.androidchatapp.ui.fragment.chat_overview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.androidchatapp.R
import com.example.androidchatapp.model.User
import com.example.androidchatapp.ui.adapter.NewChatAdapter
import com.example.androidchatapp.ui.vm.UsersViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchatapp.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_new_message.*

class NewMessageFragment : Fragment() {

    companion object {
         private const val TAG = "NEW_MESSAGE_FRAGMENT"
         const val KEY_USER = "NEW_MESSAGE_USER"
    }

    private val usersViewModel: UsersViewModel by viewModels()

    private val users = arrayListOf<User>()
    private val usersAdapter = NewChatAdapter(users, ::onNewChatClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        usersViewModel.getUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()

        // TODO show spinner when loading -> firebase is really slow sometimes

        usersViewModel.users.observe(viewLifecycleOwner, Observer {
            val uid = FirebaseAuth.getInstance().uid
            users.clear()

            //Add all users and filter out yourself
            it.forEach { user -> if(user?.uid != uid) users.add(user!!) }
            usersAdapter.notifyDataSetChanged()
        })

    }

    private fun initRv() {
        rvUsers.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        }
    }

    private fun onNewChatClick(user: User) {
        Log.i(TAG, "Starting new chat with user: ${user.username}")
        val chatIntent = Intent(context, ChatActivity::class.java)
        chatIntent.putExtra(KEY_USER, user)
        startActivity(chatIntent)
    }
}