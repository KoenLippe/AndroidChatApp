package com.example.androidchatapp.ui.vm

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidchatapp.model.ChatMessage
import com.example.androidchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChatViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val TAG = "VM_CHAT"
    }

    // List to be able to add single messages to
    private val messagesList = arrayListOf<ChatMessage>()
    // Update MutableLiveData with list from above
    private val _messages: MutableLiveData<List<ChatMessage>> = MutableLiveData(messagesList)

    val messages: LiveData<List<ChatMessage>> get() = _messages

    fun listenForMessages(chatPartner: User) {
        Log.i(TAG, "Listening for messages")

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance()
            .getReference("/user-messages/${currentUserId}/${chatPartner.uid}")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {
                    Log.i(TAG, chatMessage.content)
                    if(chatMessage.fromId == currentUserId || chatMessage.toId == currentUserId) {
                        messagesList.add(chatMessage)
                       _messages.value = messagesList
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // No action required - needs to be implemented
                return
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {
                    messagesList.remove(chatMessage)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // No action required - needs to be implemented
                return
            }

            override fun onCancelled(error: DatabaseError) {
                // No action required - needs to be implemented
                return
            }
        })
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