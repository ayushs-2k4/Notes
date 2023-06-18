package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.ayushsinghal.notes.feature.notes.util.NoteOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashScreenViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _notes = mutableStateOf<List<Note>>(emptyList())
    val notes: State<List<Note>> = _notes

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    private fun getNotes() {
//        noteUseCases.getNotesUseCase(noteOrder) // It will return the flow

        getNotesJob?.cancel()

        getNotesJob = viewModelScope.launch {
            val notes = noteUseCases.getNotesUseCase(isTrashed = true).collect {
//                originalNotes.clear()
//                originalNotes.addAll(it)

                _notes.value = it
            }
        }
    }

}