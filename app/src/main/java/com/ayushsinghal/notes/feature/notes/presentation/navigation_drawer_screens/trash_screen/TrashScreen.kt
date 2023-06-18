package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen

import android.util.Log
import android.view.MenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.components.NoteItem
import com.ayushsinghal.notes.feature.notes.util.NoteStatus
import com.ayushsinghal.notes.util.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TrashScreen(
    viewModel: TrashScreenViewModel = hiltViewModel(),
    navController: NavController
) {

    val notes = viewModel.notes

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is Trash Screen")
    }

    Scaffold(
        topBar = {
            TopBar(
                onClickBackButton = { navController.navigateUp() },
                onClickDeleteButton = {})
        }
    ) { paddingValues ->

        // Notes grid
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(notes.value) { note ->
                NoteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                        },
                    note = note,
                    onClick = {
                        Log.d(TAG, "id: ${note.id}")
                          navController.navigate("${Screen.AddEditNoteScreen.route}?noteId=${note.id}&noteStatus=${NoteStatus.TrashedNote.type}")
                    },
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onClickBackButton: () -> Unit,
    onClickDeleteButton: () -> Unit
) {

    TopAppBar(title = {
        Text(text = "Trash")
    },
        navigationIcon = {
            IconButton(onClick = { onClickBackButton() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
            }
        },
        actions = {
            IconButton(onClick = { onClickDeleteButton() }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Menu")
            }
        }
    )
}

data class MyMenuItem(val title: String, val icon: ImageVector)

@Preview(showSystemUi = true)
@Composable
fun TrashScreenPreview() {
    TrashScreen(
        navController = NavController(LocalContext.current)
    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        onClickBackButton = {},
        onClickDeleteButton = {}
    )
}