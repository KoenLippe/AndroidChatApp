package com.example.androidchatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidchatapp.model.User

class ChatActivity : AppCompatActivity() {

    lateinit var chatPartner: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))

        chatPartner = intent.getParcelableExtra(ChatOverviewActivity.KEY_USER)!!
        supportActionBar?.title = chatPartner.username
    }
}