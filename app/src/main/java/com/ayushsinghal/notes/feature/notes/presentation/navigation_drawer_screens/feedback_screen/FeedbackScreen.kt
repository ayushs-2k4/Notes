package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.feedback_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FeedbackScreen(
    viewModel: FeedbackScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = {
            viewModel.onClickEmail(context)
        }) {
            Text(text = "Email Me")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FeedbackScreenPreview() {
    FeedbackScreen()
}