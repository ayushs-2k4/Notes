package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    isSingleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit
) {
    Box(modifier = modifier)
    {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                },
            textStyle = textStyle.copy(
                color =
                    if(isUsingNightModeResources())
                    {
                        Color.White
                    }else{
                        Color.Black
                    }
            ),
            singleLine = isSingleLine,
        )

        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
            )
        }
    }
}


@Composable
private fun isUsingNightModeResources(context: Context = LocalContext.current): Boolean {
    return when (context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> false
    }
}

@Preview(showSystemUi = true)
@Composable
fun TransparentHintTextFieldPreview() {
    TransparentHintTextField(text = "Text", hint = "Hint", onValueChange = {}, onFocusChange = {})
}