package com.example.androidchatapp.ui.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidchatapp.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class LatestMessagesViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val TAG = "VM_LATEST_MESSAGES"
    }

    // List to be able to add single messages to
    private val latestMessagesMap = hashMapOf<String, ChatMessage>()
    // Update MutableLiveData with list from above
    private val _latestMessages: MutableLiveData<List<ChatMessage>> =
        MutableLiveData()

    val latestMessages: LiveData<List<ChatMessage>> get() = _latestMessages

    fun getLatestMessages() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val latestMessageRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/${currentUserId}")

        latestMessageRef.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d(TAG, "Message: " + chatMessage.toString())

                if(chatMessage != null) {
                    latestMessagesMap[snapshot.key!!] = chatMessage
                    _latestMessages.value = latestMessagesMap.values.toList()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {
                    latestMessagesMap[snapshot.key!!] = chatMessage
                    _latestMessages.value = latestMessagesMap.values.toList()
                }
                return
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {
                    latestMessagesMap[snapshot.key!!] = chatMessage
                    _latestMessages.value = latestMessagesMap.values.toList()
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
}