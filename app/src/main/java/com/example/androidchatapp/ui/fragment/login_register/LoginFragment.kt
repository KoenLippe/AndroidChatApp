package com.example.androidchatapp.ui.fragment.login_register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidchatapp.LoginRegisterActivity
import com.example.androidchatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment: Fragment() {

    companion object {
        const val TAG = "FRAGMENT_LOGIN"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }

        btnLogin.setOnClickListener {
            onLogin()
        }
    }

    private fun onLogin() {
        // TODO: Possible addition: add loader
        val username = txtUsernameLogin.editText?.text.toString()
        val password = txtPasswordLogin.editText?.text.toString()

        if(username.isBlank() or password.isBlank()) {
            Toast.makeText(context, "Please fill in all fields ",
                Toast.LENGTH_LONG).show()
            return
        }

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "Login successful ${task.result?.user?.uid}")

                (activity as LoginRegisterActivity).startChatOverviewActivity()

            }
            .addOnFailureListener {error ->
                Log.d(TAG, "signIn:failure", error.cause)
                Toast.makeText(context, "Login failed, reason: ${error.message}",
                    Toast.LENGTH_LONG).show()
            }
    }
}