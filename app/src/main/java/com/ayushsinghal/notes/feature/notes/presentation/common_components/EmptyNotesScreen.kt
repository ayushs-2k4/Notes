package com.ayushsinghal.notes.feature.notes.presentation.common_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmptyNotesScreen(
    modifier: Modifier = Modifier,
    image: Any,
    tintColor:Color = Color(0xFFFABD04),
    subText:String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        if(image is ImageVector){
        Image(
            imageVector = image,
            contentDescription = null,
            colorFilter = ColorFilter.tint(tintColor),
            modifier = Modifier.size(130.dp)
        )
        }else if(image is Int){
            Image(
                painter = painterResource(image),
                contentDescription = null,
                colorFilter = ColorFilter.tint(tintColor),
                modifier = Modifier.size(130.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = subText)
    }
}

@Preview
@Composable
fun EmptyNotesScreenPreview() {
    EmptyNotesScreen(
        image = Icons.Outlined.Lightbulb,
        subText = "Notes you add appear here"
    )
}