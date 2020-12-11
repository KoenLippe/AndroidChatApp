package com.example.androidchatapp.ui.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidchatapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        const val TAG = "USER_VIEW_MODEL"
    }

    //TODO: Add repository?
    private val _users: MutableLiveData<List<User?>> = MutableLiveData()
    private val _fetching: MutableLiveData<Boolean> = MutableLiveData(false)

    val users: LiveData<List<User?>> get() = _users
    val fetching: LiveData<Boolean> get() = _fetching
    //TODO add spinner when loading

    fun getUsers() {
        // Fetching users from database
        _fetching.value = true
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val convertedUsers: List<User?> = snapshot.children.map {
                    it!!.getValue(User::class.java)
                }

                _users.value = convertedUsers
                _fetching.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "addListenerForSingleValueEvent Cancelled")
            }
        })
    }
}