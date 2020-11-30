package com.example.androidchatapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.androidchatapp.R
import com.example.androidchatapp.model.User
import com.example.androidchatapp.ui.UsersAdapter
import com.example.androidchatapp.ui.vm.UsersViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_new_message.*

class NewMessageFragment : Fragment() {

    private val usersViewModel: UsersViewModel by viewModels()

    private val users = arrayListOf<User>()
    private val usersAdapter = UsersAdapter(users)

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

        usersViewModel.users.observe(viewLifecycleOwner, Observer {
            users.clear()
            it.forEach { user -> users.add(user!!) }
            usersAdapter.notifyDataSetChanged()
        })

    }

    private fun initRv() {
        rvUsers.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL , false)
        }
    }
}