package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeleteDialog(
    message: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = { onDeleteClick() }) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancelClick() }) {
                Text(text = dismissButtonText)
            }
        }
    )
}

@Preview
@Composable
fun DeleteDialogPreview() {
    DeleteDialog(
        message = "Do you want to delete note",
        dismissButtonText = "Dismiss",
        confirmButtonText = "Delete",
        onCancelClick = {},
        onDeleteClick = {}
    )
}