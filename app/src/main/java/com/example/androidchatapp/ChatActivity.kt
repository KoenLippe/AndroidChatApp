package com.example.androidchatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidchatapp.model.User
import com.example.androidchatapp.ui.fragment.chat_overview.NewMessageFragment

class ChatActivity : AppCompatActivity() {

    lateinit var chatPartner: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))

        chatPartner = intent.getParcelableExtra<User>(NewMessageFragment.KEY_USER)!!
        supportActionBar?.title = chatPartner.username
    }
}