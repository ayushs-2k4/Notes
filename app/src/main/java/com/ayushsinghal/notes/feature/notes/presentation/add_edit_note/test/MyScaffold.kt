package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ayushsinghal.notes.R
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TransparentHintTextField

@Composable
fun MyScaffold() {

    Box(Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.note_background_light_celebration_0714),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Transparent)
//        ) {
//            Column {
//                TransparentHintTextField(
//                    givenText = title,
//                    hint = "Title",
//                    onValueChange = {},
//                    enabled = true,
//                    onFocusChange = {}
//                )
//                TextField(value = title, onValueChange = { title = it })
//                TextField(value = content, onValueChange = { content = it })
//            }
//        }

        var title by remember {
            mutableStateOf("")
        }

        var content by remember {
            mutableStateOf("")
        }

        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            topBar = { MyTop() },
            bottomBar = { MyBottom() }
        ) {paddingValues->
            paddingValues

            Column() {
                TransparentHintTextField(
                    givenText = title,
                    hint = "Title",
                    onValueChange = { title = it },
                    enabled = true,
                    onFocusChange = {}
                )

                TransparentHintTextField(
                    givenText = content,
                    hint = "Content",
                    onValueChange = { content = it },
                    enabled = true,
                    onFocusChange = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTop() {
    TopAppBar(title = { /*TODO*/ }
    , colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    ))
}

@Composable
fun MyBottom() {
    BottomAppBar(
        containerColor = Color.Transparent
    ) {
        IconButton(onClick = {  }) {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = "New Icon"
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MyScaffoldPreview() {
    MyScaffold()
}

@Preview
@Composable
fun MyBottomPreview() {
    MyBottom()
}