package com.example.androidchatapp.ui.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.androidchatapp.LoginRegisterActivity
import com.example.androidchatapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class LogoutDialog {
    companion object {
        fun createDialog(context: Context): MaterialAlertDialogBuilder {
            return MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.help_are_you_sure))
                .setMessage(context.getString(R.string.help_logout_text))
                .setNegativeButton(context.getString(R.string.help_no)) { _, _ ->
                    // Do nothing here
                }
                .setPositiveButton(context.getString(R.string.help_yes)) { _, _ ->
                    FirebaseAuth.getInstance().signOut()

                    val loginIntent = Intent(context, LoginRegisterActivity::class.java)
                    startActivity(context, loginIntent, Bundle())
                }
        }
    }
}