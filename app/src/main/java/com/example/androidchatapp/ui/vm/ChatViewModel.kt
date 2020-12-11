package com.example.androidchatapp.ui.vm

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val TAG = "CHAT_VIEW_MODEL"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(to: User, message: String) {
        Log.i(TAG, "Attempting to send message")

        val fromId = FirebaseAuth.getInstance().currentUser?.uid

        // Because of NoSQL database structure we need to update on multiple places
        val ownRef = FirebaseDatabase.getInstance().getReference("/user-messages/${fromId}/${to.uid}").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/${to.uid}/${fromId}").push()
        val ownLatestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/${fromId}/${to.uid}")
        val toLatestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/${to.uid}/${fromId}")

        val chatMessage = ChatMessage(
            ownRef.key!!,
            message,
            fromId!!,
            to.uid!!,
            System.currentTimeMillis()/1000)

        ownRef.setValue(chatMessage).addOnCompleteListener {
            Log.i(TAG, "Chat Message Send - ownReference")
        }
        toRef.setValue(chatMessage).addOnCompleteListener {
            Log.i(TAG, "Chat Message Send - toReference")
        }
        ownLatestMessageRef.setValue(chatMessage).addOnCompleteListener {
            Log.i(TAG, "Latest message updated - ownReference")
        }
        toLatestMessageRef.setValue(chatMessage).addOnCompleteListener {
            Log.i(TAG, "Latest message updated - toReference")
        }
    }
}