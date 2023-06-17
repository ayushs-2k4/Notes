package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeleteDialog(
    message: String,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(onClick = {onDeleteClick()}) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            Button(onClick = {onCancelClick()}) {
                Text(text = "Cancel")
            }
        }
    )
}

@Preview
@Composable
fun DeleteDialogPreview() {
    DeleteDialog(
        message = "Do you want to delete note",
        onCancelClick = {},
        onDeleteClick = {}
    )
}