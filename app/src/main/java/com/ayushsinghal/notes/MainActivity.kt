package com.ayushsinghal.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ayushsinghal.notes.feature.authentication.presentation.signin.SignInScreen
import com.ayushsinghal.notes.feature.authentication.presentation.signin.SignInViewModel
import com.ayushsinghal.notes.feature.authentication.presentation.signup.SignUpScreen
import com.ayushsinghal.notes.feature.authentication.presentation.signup.SignUpViewModel
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.AddEditNoteScreen
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.AllNotesScreen
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen.ArchiveScreen
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.feedback_screen.FeedbackScreen
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen.TrashScreen
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.settings_screen.SettingsScreen
import com.ayushsinghal.notes.feature.notes.util.NoteStatus
import com.ayushsinghal.notes.ui.theme.NotesTheme
import com.ayushsinghal.notes.util.NavigationDrawerScreen
import com.ayushsinghal.notes.util.Screen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        WindowCompat.setDecorFitsSystemWindows(window, false)
//        val windowInsetsController =
//            ViewCompat.getWindowInsetsController(window.decorView)
//        windowInsetsController?.isAppearanceLightNavigationBars = true


        setContent {
            NotesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.AllNotesScreen.route
                    )
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
                                        navController.navigate(Screen.AllNotesScreen.route)
                                    }
                                },
                                onSignInInsteadButtonClicked = {
                                    navController.navigate("SignInScreen")
                                })
                        }

                        composable(route = Screen.AllNotesScreen.route)
                        {
                            AllNotesScreen(
                                navController = navController
                            )
                        }

                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteStatus={noteStatus}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId",
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteStatus"
                                ) {
                                    type = NavType.StringType
//                                    defaultValue = NoteStatus.NewNote.type
                                }
                            )
                        )
                        {
                            AddEditNoteScreen(navController = navController)
                        }

                        composable(route = NavigationDrawerScreen.Archive.route)
                        {
                            ArchiveScreen()
                        }

                        composable(route = NavigationDrawerScreen.Trash.route)
                        {
                            TrashScreen(navController = navController)
                        }

                        composable(route = NavigationDrawerScreen.Settings.route)
                        {
                            SettingsScreen()
                        }

                        composable(route = NavigationDrawerScreen.Feedback.route)
                        {
                            FeedbackScreen()
                        }
                    }
                }
            }
        }
    }
}