package com.ayushsinghal.notes.feature.notes.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.room.Room
import com.ayushsinghal.notes.feature.notes.data.local.NoteDatabase
import com.ayushsinghal.notes.feature.notes.data.repository.NoteRepositoryImpl
import com.ayushsinghal.notes.feature.notes.domain.usecase.AddNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.DeleteNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.GetNotesUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.NoteUseCases
import com.ayushsinghal.notes.feature.notes.presentation.components.NoteItem
import com.ayushsinghal.notes.feature.notes.presentation.components.OrderSection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AllNotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(SnackbarHostState()) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Notes",
                    style = MaterialTheme.typography.headlineLarge
                )

                IconButton(onClick = {
                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                }
            }
        }

        AnimatedVisibility(
            visible = state.isOrderSectionVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                noteOrder = state.noteOrder,
                onOrderChange = {
                    viewModel.onEvent(NotesEvent.Order(it))
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        )
        {
            items(state.notes)
            {
                NoteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        },
                    note = it,
                    onDeleteClick = {
                        viewModel.onEvent(NotesEvent.DeleteNote(it))

                        scope.launch {
                            val result = snackBarHostState.showSnackbar(
                                message = "Note Deleted",
                                actionLabel = "Undo"
                            )

                            if(result==SnackbarResult.ActionPerformed)
                            {
                                viewModel.onEvent(NotesEvent.RestoreNote)
                            }
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun MyFloatingActionButton() {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Create, contentDescription = "Add Note")
    }
}

@Preview(showSystemUi = true)
@Composable
fun AllNotesScreenPreview() {

    val noteDatabase = Room
        .databaseBuilder(
            LocalContext.current,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
        .build()

    val noteRepositoryImpl = NoteRepositoryImpl(noteDatabase.noteDao)

    val notesUseCases = NoteUseCases(
        addNoteUseCase = AddNoteUseCase(noteRepositoryImpl),
        deleteNoteUseCase = DeleteNoteUseCase(noteRepositoryImpl),
        getNotesUseCase = GetNotesUseCase(noteRepositoryImpl)
    )

    AllNotesScreen(
        navController = NavController(LocalContext.current),
        viewModel = NotesViewModel(notesUseCases)
    )
}

//@Preview
//@Composable
//fun MyFloatingActionButtonPreview() {
//    MyFloatingActionButton()
//}