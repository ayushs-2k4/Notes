package com.ayushsinghal.notes.feature.notes.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onCheck: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onCheck)

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = text)
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun DefaultRadioButtonPreview() {
//    DefaultRadioButton(text = "Ayush",selected = false, onCheck = {})
//}