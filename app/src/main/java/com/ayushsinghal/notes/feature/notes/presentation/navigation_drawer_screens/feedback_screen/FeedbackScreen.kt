package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.feedback_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FeedbackScreen(
    viewModel: FeedbackScreenViewModel = hiltViewModel(),
    navController: NavController
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                onClickBackButton = { navController.navigateUp() }
            )
        }
    ) {paddingValues->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Button(onClick = {
                viewModel.onClickEmail(context)
            }) {
                Text(text = "Email Me")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onClickBackButton: () -> Unit,
) {

    TopAppBar(title = {
        Text(text = "Archive")
    },
        navigationIcon = {
            IconButton(onClick = { onClickBackButton() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
            }
        },
        actions = {
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun FeedbackScreenPreview() {
    FeedbackScreen(
        navController = NavController(LocalContext.current)
    )
}