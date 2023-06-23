package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagInputDialog(
    isNewTag: Boolean,
    tagText: String,
    onClickAddOrUpdateTag: (String) -> Unit,
    onClickCancelOrDelete: () -> Unit
) {
    val inputText = remember { mutableStateOf(tagText) }

    AlertDialog(
        onDismissRequest = {
//            onClickCancelOrDelete()
        },
        title = { Text(text = "Add Tag") },
        text = {
            OutlinedTextField(
                value = inputText.value,
                onValueChange = { inputText.value = it },
                label = { Text("Enter Tag") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onClickAddOrUpdateTag(inputText.value)
            }) {
                Text(
                    text = if (isNewTag) {
                        "Add"
                    } else {
                        "Save"
                    }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onClickCancelOrDelete()
            }) {
                Text(
                    text = if (isNewTag) {
                        "Cancel"
                    } else {
                        "Delete"
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun TagInputDialogTruePreview() {
    TagInputDialog(
        isNewTag = true,
        tagText = "",
        onClickAddOrUpdateTag = {},
        onClickCancelOrDelete = {})
}

@Preview
@Composable
fun TagInputDialogFalsePreview() {
    TagInputDialog(
        isNewTag = false,
        tagText = "Existing Tag",
        onClickAddOrUpdateTag = {},
        onClickCancelOrDelete = {})
}