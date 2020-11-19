package com.example.androidchatapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidchatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: Fragment() {
    companion object {
        const val TAG = "REGISTER"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        icon_back.setOnClickListener {
            findNavController().popBackStack()
        }

        btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val username = txtUsernameRegister.editText?.text.toString()
        val password = txtPasswordRegister.editText?.text.toString()
        Log.d(TAG, username)
        Log.d(TAG, password)

        // TODO Add validation (password has to be 6 chars long)
        // TODO add hashing?

        Log.d(TAG, "Attempting signup")
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) return@addOnCompleteListener

                val user = firebaseAuth.currentUser
                Log.d(TAG, "Registered user with email: ${user?.email}")
                Toast.makeText(context, "Registration successful ${user?.uid}",
                    Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {error ->
                Log.d(TAG, "createUserWithEmail:failure", error.cause)
                Toast.makeText(context, "Authentication failed, reason: ${error.message}",
                    Toast.LENGTH_LONG).show()
            }
    }
}