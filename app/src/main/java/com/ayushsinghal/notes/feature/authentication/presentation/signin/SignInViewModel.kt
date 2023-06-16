package com.ayushsinghal.notes.feature.authentication.presentation.signin

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignInViewModel : ViewModel() {

    private val auth = Firebase.auth

    private val _isUserSignedIn = MutableStateFlow<Boolean>(false)
    val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        checkUserSignedIn()
    }

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    fun isValidCredentials(): Boolean {
        if (email.value.isNotBlank() && password.value.isNotBlank()) {
            return true
        } else {
            _errorMessage.value = "Password and Email can not be empty"
            return false
        }
    }

    fun signIn() {
        auth.signInWithEmailAndPassword(email.value, password.value).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _isUserSignedIn.value = true
            } else {
                _errorMessage.value = task.exception?.message.toString()
            }
        }
    }

//    fun signOut() {
//        auth.signOut()
//        // Update the authentication state
//        _isUserSignedIn.value = false
//    }

    private fun checkUserSignedIn() {
        Log.d(TAG, auth.currentUser.toString())
        _isUserSignedIn.value = auth.currentUser != null
    }
}