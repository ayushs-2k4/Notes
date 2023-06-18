package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.feedback_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ayushsinghal.notes.feature.notes.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackScreenViewModel @Inject constructor() : ViewModel() {

    @SuppressLint("QueryPermissionsNeeded")
    val onClickEmail: (context: Context) -> Unit = { context ->
        val subject = "Hello"
        val body = "This is the email body."

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Set the URI scheme to "mailto:"
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(Constants.emailAddress)
            ) // Set the recipient email address
            putExtra(Intent.EXTRA_SUBJECT, subject) // Set the email subject
            putExtra(Intent.EXTRA_TEXT, body) // Set the email body
        }

        context.startActivity(intent)
    }
}