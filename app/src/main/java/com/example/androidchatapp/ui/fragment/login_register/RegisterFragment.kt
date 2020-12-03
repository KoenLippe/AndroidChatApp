package com.example.androidchatapp.ui.fragment.login_register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidchatapp.R
import com.example.androidchatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: Fragment() {
    companion object {
        const val TAG = "REGISTER"
        const val MINIMUM_PASSWORD_LENGTH = 6
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
        val passwordConfirm = txtPasswordConfirm.editText?.text.toString()
        Log.d(TAG, username)
        Log.d(TAG, password)

        // TODO add hashing?

        if(username.isBlank() or password.isBlank() or passwordConfirm.isBlank()) {
            Toast.makeText(context, getString(R.string.register_error_fill_all_fields),
                Toast.LENGTH_LONG).show()
            return
        } else if(password.length >= MINIMUM_PASSWORD_LENGTH ||
            passwordConfirm.length >= MINIMUM_PASSWORD_LENGTH
        ) {
                // TODO Test with this
                Toast.makeText(context, getString(R.string.register_error_pass_length),
                    Toast.LENGTH_LONG).show()
                return
        }
        if (password != passwordConfirm) {
            Toast.makeText(context, getString(R.string.register_error_password_not_equal),
                Toast.LENGTH_LONG).show()
            return
        }

        Log.d(TAG, "Attempting signup")
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) return@addOnCompleteListener

                val user: FirebaseUser? = firebaseAuth.currentUser
                Log.d(TAG, "Registered user with email: ${user?.email}")
                Toast.makeText(context, "Registration successful ${user?.uid}",
                    Toast.LENGTH_LONG).show()

                saveUserToFirebaseDatabase(User(user?.uid, username))
                findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
            }
            .addOnFailureListener {error ->
                Log.d(TAG, "createUserWithEmail:failure", error.cause)
                Toast.makeText(context, "Authentication failed, reason: ${error.message}",
                    Toast.LENGTH_LONG).show()
            }
    }
    
    private fun saveUserToFirebaseDatabase(user: User) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/${user.uid}")

        ref.setValue(user).addOnCompleteListener {
            Log.d(TAG, "Saved user to firebase database")
        }
    }
}