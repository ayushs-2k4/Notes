package com.ayushsinghal.notes.feature.authentication.presentation.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayushsinghal.notes.feature.authentication.presentation.signin.SignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel,
    onSignUpButtonClicked: () -> Unit,
    onSignInInsteadButtonClicked: () -> Unit
) {
    var email by remember { signUpViewModel.email }
    var password by remember { signUpViewModel.password }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = "Email") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = password, onValueChange = {
                password = it
            },
            label = { Text(text = "Password") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(100.dp))

        Button(onClick = onSignUpButtonClicked) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onSignInInsteadButtonClicked) {
            Text(text = "Sign In Instead")
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(SignUpViewModel(), {}, {})
}