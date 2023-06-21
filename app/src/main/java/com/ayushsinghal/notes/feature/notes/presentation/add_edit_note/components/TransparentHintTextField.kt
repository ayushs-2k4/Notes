package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TransparentHintTextField(
    givenText: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    onFocusChange: (FocusState) -> Unit
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
//        disabledTextColor = Color.Black.copy(),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
//        focusedTextColor = Color.Red,
//        unfocusedTextColor = Color.Yellow,
    )

    TextField(
        value = givenText,
        onValueChange = onValueChange,
        singleLine = singleLine,
        textStyle = textStyle,
        enabled = enabled,
        colors = textFieldColors,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                onFocusChange(it)
            },
        placeholder = {
            Text(
                text = hint,
                style = textStyle
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Preview(showSystemUi = true)
@Composable
fun TransparentHintTextFieldPreview() {
    TransparentHintTextField(
        givenText = "Text",
        hint = "Hint",
        enabled = true,
        onValueChange = {},
        onFocusChange = {}
    )
}