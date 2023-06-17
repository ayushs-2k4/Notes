package com.ayushsinghal.notes.feature.notes.presentation.all_notes

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.room.Room
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.data.local.NoteDatabase
import com.ayushsinghal.notes.feature.notes.data.repository.NoteRepositoryImpl
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.AddNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.DeleteNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.GetNotesUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.SearchNotesUseCase
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.MySearchBar
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.components.NoteItem
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.components.OrderSection
import com.ayushsinghal.notes.util.Screen
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

    val searchResultsKey = remember { mutableStateOf(0) }

    Scaffold(
//                viewModel.onEvent(NotesEvent.ToggleOrderSection)
        topBar = {
            TopBar(
                onSortButtonPressed = { viewModel.onEvent(NotesEvent.ToggleOrderSection) },
                onMenuButtonPressed = {},
                onQueryChange = {
                    viewModel.onEvent(NotesEvent.SearchNote(it))
                    searchResultsKey.value++
                },
                onSearch = {
                    viewModel.onEvent(NotesEvent.SearchNote(it))
                    searchResultsKey.value++
                }
            )
        },
        snackbarHost = { SnackbarHost(SnackbarHostState()) },
        floatingActionButton = { MyFloatingActionButton(navController = navController) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val offsetYOfOrderSection by animateDpAsState(
                targetValue = if (state.isOrderSectionVisible) 0.dp else (-30).dp,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            val offsetYOfLazyStaggeredGrid by animateDpAsState(
                targetValue = if (state.isOrderSectionVisible) 124.dp else 0.dp,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            // Order section
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .alpha(if (state.isOrderSectionVisible) 1f else 0f)
                    .offset(y = offsetYOfOrderSection),
                noteOrder = state.noteOrder,
                onOrderChange = {
                    viewModel.onEvent(NotesEvent.Order(it))
                }
            )

            // Notes grid
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = offsetYOfLazyStaggeredGrid)
            ) {
                items(state.notes) { note ->
                    NoteItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                            },
                        note = note,
                        onClick = {
                            Log.d(TAG, "id: ${note.id}")
                            navController.navigate(
                                Screen.AddEditNoteScreen.route +
                                        "?noteId=${note.id}"
                            )
                        },
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))

                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Note Deleted",
                                    actionLabel = "Undo"
                                )

                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MyFloatingActionButton(
    navController: NavController
) {
    FloatingActionButton(onClick = {
        navController.navigate(Screen.AddEditNoteScreen.route)
    }
    ) {
        Icon(imageVector = Icons.Default.Create, contentDescription = "Add Note")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onSortButtonPressed: () -> Unit,
    onMenuButtonPressed: () -> Unit,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Your Notes")
        },
        actions = {

            MySearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                onSortButtonPressed = { onSortButtonPressed() },
                onMenuButtonPressed = { onMenuButtonPressed() },
                onQueryChange = { onQueryChange(it) },
                onSearch = { onSearch(it) }
            )

//            IconButton(onClick = { onSortButtonPressed() }) {
//                Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
//            }
        }
    )
}

//@Preview(showSystemUi = true)
//@Composable
//fun AllNotesScreenPreview() {
//
//    val noteDatabase = Room
//        .databaseBuilder(
//            LocalContext.current,
//            NoteDatabase::class.java,
//            NoteDatabase.DATABASE_NAME
//        )
//        .build()
//
//    val noteRepositoryImpl = NoteRepositoryImpl(noteDatabase.noteDao)
//
//    val notesUseCases = NoteUseCases(
//        addNoteUseCase = AddNoteUseCase(noteRepositoryImpl),
//        deleteNoteUseCase = DeleteNoteUseCase(noteRepositoryImpl),
//        getNotesUseCase = GetNotesUseCase(noteRepositoryImpl),
//        searchNotesUseCase = SearchNotesUseCase(noteRepositoryImpl)
//    )
//
//    AllNotesScreen(
//        navController = NavController(LocalContext.current),
//        viewModel = NotesViewModel(notesUseCases)
//    )
//}

//@Preview
//@Composable
//fun MyFloatingActionButtonPreview() {
//    MyFloatingActionButton()
//}