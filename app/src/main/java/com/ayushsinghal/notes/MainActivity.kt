package com.ayushsinghal.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayushsinghal.notes.feature.authentication.presentation.signin.SignInScreen
import com.ayushsinghal.notes.feature.authentication.presentation.signin.SignInViewModel
import com.ayushsinghal.notes.feature.authentication.presentation.signup.SignUpScreen
import com.ayushsinghal.notes.feature.authentication.presentation.signup.SignUpViewModel
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.AllNotesScreen
import com.ayushsinghal.notes.ui.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "AllNotesScreen")
                    {
                        composable(route = "SignInScreen")
                        {

                            val signInViewModel = SignInViewModel()

                            SignInScreen(
                                onSignUpInsteadButtonClicked = {
                                    navController.navigate("SignUpScreen")
                                },
                                navController = navController,
                                signInViewModel = signInViewModel
                            )
                        }

                        composable(route = "SignUpScreen")
                        {
                            val signUpViewModel = SignUpViewModel()

                            SignUpScreen(
                                signUpViewModel = signUpViewModel,
                                onSignUpButtonClicked = {
                                    if (signUpViewModel.isValidCredentials()) {
                                        navController.navigate("AllNotesScreen")
                                    }
                                },
                                onSignInInsteadButtonClicked = {
                                    navController.navigate("SignInScreen")
                                })
                        }

                        composable(route = "AllNotesScreen")
                        {
                            AllNotesScreen(
                                navController = navController
                            )
                        }

                        composable(route = "AddNoteUseCase")
                        {
                        }
                    }
                }
            }
        }
    }
}