package com.example.androidchatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.androidchatapp.model.User
import com.example.androidchatapp.ui.dialog.LogoutDialog
import com.example.androidchatapp.ui.fragment.chat_overview.NewMessageFragment
import kotlinx.android.synthetic.main.activity_chat_overview.*
import kotlinx.android.synthetic.main.content_chat_overview.*

class ChatOverviewActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ACTIVITY_CHATOVERVIEW"
        const val KEY_USER = "NEW_MESSAGE_USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_overview)
        setSupportActionBar(findViewById(R.id.toolbarChatOverview))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { _ ->
            nav_host_fragment.findNavController()
                .navigate(R.id.action_ChatOverviewFragment_to_NewMessageFragment)
        }

        fabToggler()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_overview, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_button_sign_out -> {
                LogoutDialog.createDialog(this).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun startChatActivity(user: User) {
        Log.i(TAG, "Starting new chat with user: ${user.username}")
        val chatIntent = Intent(this, ChatActivity::class.java)
        chatIntent.putExtra(KEY_USER, user)
        startActivity(chatIntent)
    }

    private fun fabToggler() {
        nav_host_fragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(R.id.NewChatFragment)) {
                fab.hide()
            } else {
                fab.show()
            }
        }
    }
}