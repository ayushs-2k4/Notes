package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ayushsinghal.notes.R
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.components.NoteItem
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen.TopBar
import com.ayushsinghal.notes.feature.notes.util.NoteStatus
import com.ayushsinghal.notes.util.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArchiveScreen(
    archiveScreenViewModel: ArchiveScreenViewModel= hiltViewModel(),
    navController: NavController
) {
    val notes = archiveScreenViewModel.notes

    Scaffold(
        topBar = {
            TopBar(
                notesSize = notes.value.size,
                onClickBackButton = { navController.navigateUp() })
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
                        navController.navigate("${Screen.AddEditNoteScreen.route}?noteId=${note.id}&noteStatus=${NoteStatus.ArchivedNote.type}")
                    },
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    notesSize: Int,
    onClickBackButton: () -> Unit,
) {

    TopAppBar(title = {
        Text(text = "Archive")
    },
        navigationIcon = {
            IconButton(onClick = { onClickBackButton() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
            }
        },
        actions = {
        }
    )
}