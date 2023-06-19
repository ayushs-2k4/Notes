package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.archive.ArchiveUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveScreenViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val archiveUseCases: ArchiveUseCases
) : ViewModel() {

    private val _notes = mutableStateOf<List<Note>>(emptyList())
    val notes: State<List<Note>> = _notes

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    fun onArchiveEvent(archiveEvent: ArchiveEvent) {
        when (archiveEvent) {
            is ArchiveEvent.MoveNoteToArchive -> {
                viewModelScope.launch {
                    archiveUseCases.archiveNoteUseCase(archiveEvent.id)
                }
            }

            is ArchiveEvent.MoveNoteFromArchive -> {
                viewModelScope.launch {
                    archiveUseCases.unArchiveNoteUseCase(archiveEvent.id)
                }
            }

        }
    }

    private fun getNotes() {
//        noteUseCases.getNotesUseCase(noteOrder) // It will return the flow

        getNotesJob?.cancel()

        getNotesJob = viewModelScope.launch {
            val notes = noteUseCases.getNotesUseCase(isTrashed = false, isArchived = true).collect {

                _notes.value = it
            }
        }
    }
}