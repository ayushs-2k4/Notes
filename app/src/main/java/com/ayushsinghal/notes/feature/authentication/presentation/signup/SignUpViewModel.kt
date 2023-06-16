package com.ayushsinghal.notes.feature.authentication.presentation.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    fun isValidCredentials(): Boolean {
        return true
    }
}