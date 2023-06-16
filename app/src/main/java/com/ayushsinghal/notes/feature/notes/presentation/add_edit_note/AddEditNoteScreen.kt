package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.room.Room
import com.ayushsinghal.notes.feature.notes.data.local.NoteDatabase
import com.ayushsinghal.notes.feature.notes.data.repository.NoteRepositoryImpl
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.AddEditNoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.DeleteNoteAddEditUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.GetNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.AddNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.DeleteNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.GetNotesUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TransparentHintTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    Scaffold(
        topBar = {
            TopBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        paddingValues;

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                textStyle = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight()
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
            }
        },
        actions = {
            IconButton(onClick = {
                viewModel.onEvent(AddEditNoteEvent.DeleteNote)
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun AddEditNoteScreenPreview() {

    val noteDatabase = Room
        .databaseBuilder(
            LocalContext.current,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
        .build()

    val noteRepository = NoteRepositoryImpl(noteDatabase.noteDao)


    val noteUseCases = NoteUseCases(
        addNoteUseCase = AddNoteUseCase(noteRepository),
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
        getNotesUseCase = GetNotesUseCase(noteRepository)
    )

    val addEditNoteUseCases = AddEditNoteUseCases(
        deleteNoteAddEditUseCase = DeleteNoteAddEditUseCase(noteRepository),
        getNoteUseCase = GetNoteUseCase(noteRepository)
    )

    val addEditNoteViewModel = AddEditNoteViewModel(
        noteUseCases = noteUseCases,
        addEditNoteUseCases = addEditNoteUseCases,
       savedStateHandle =  SavedStateHandle(),
    )

    AddEditNoteScreen(navController = NavController(LocalContext.current), viewModel = addEditNoteViewModel)
}

@Preview
@Composable
fun TopBarPreview() {

    val noteDatabase = Room
        .databaseBuilder(
            LocalContext.current,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
        .build()

    val noteRepository = NoteRepositoryImpl(noteDatabase.noteDao)


    val noteUseCases = NoteUseCases(
        addNoteUseCase = AddNoteUseCase(noteRepository),
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
        getNotesUseCase = GetNotesUseCase(noteRepository)
    )

    val addEditNoteUseCases = AddEditNoteUseCases(
         deleteNoteAddEditUseCase = DeleteNoteAddEditUseCase(noteRepository),
        getNoteUseCase = GetNoteUseCase(noteRepository)
    )

    val addEditNoteViewModel = AddEditNoteViewModel(
        noteUseCases = noteUseCases,
        addEditNoteUseCases = addEditNoteUseCases,
        savedStateHandle =   SavedStateHandle()
    )

    TopBar(
        viewModel = addEditNoteViewModel,
        navController = NavController(LocalContext.current)
    )
}