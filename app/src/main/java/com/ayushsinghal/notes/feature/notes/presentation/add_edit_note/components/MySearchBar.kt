package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    modifier: Modifier = Modifier,
    onSortButtonPressed: () -> Unit,
    onMenuButtonPressed: () -> Unit,
    onQueryChange:(String)->Unit,
    onSearch:(String)->Unit
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var paddingValue by remember { mutableStateOf(10.dp) }
    val animatedPaddingValue by animateDpAsState(
        targetValue = paddingValue,
        animationSpec = tween(durationMillis = 100),
        label = ""
    )

    SearchBar(
        query = text,
        onQueryChange = {
            onQueryChange(it)
            text = it
            Log.d(TAG, "onQueryChange: $it")
        },
        onSearch = {
            onSearch(it)
            active = false
            Log.d(TAG, "onSearch: $it")
        },
        active = active,
        onActiveChange = {
//            active = it
            if (active) {
                paddingValue = 0.dp
            } else {
                paddingValue = 10.dp
            }
        },
        placeholder = {
            Text(text = "Search your Notes")
        },
        leadingIcon = {
            IconButton(onClick = { onMenuButtonPressed()}) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon")
                }
        },
        trailingIcon = {
            IconButton(onClick = { onSortButtonPressed() }) {
                Icon(imageVector = Icons.Default.Sort, contentDescription = null)
            }
        },
        modifier = modifier
            .padding(
                horizontal = animatedPaddingValue
            )
    ) {
    }
}

@Preview(showSystemUi = true)
@Composable
fun MySearchBarPreview() {
    MySearchBar(
        onSortButtonPressed = {},
        onMenuButtonPressed = {},
        onQueryChange = {},
        onSearch = {}
    )
}