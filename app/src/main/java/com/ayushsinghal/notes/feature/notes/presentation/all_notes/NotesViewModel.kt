package com.ayushsinghal.notes.feature.notes.presentation.all_notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.ayushsinghal.notes.feature.notes.util.NoteOrder
import com.ayushsinghal.notes.feature.notes.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf<NotesState>(NotesState())
    val state: State<NotesState> = _state

    private val originalNotes = mutableListOf<Note>()


    private var lastDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.LastModifiedDate(OrderType.Descending))
    }

    fun onEvent(notesEvent: NotesEvent) {
        when (notesEvent) {
            is NotesEvent.Order -> {
                // Checking if order is changed
                if ((_state.value.noteOrder::class == notesEvent.noteOrder::class) && (_state.value.noteOrder.orderType == notesEvent.noteOrder.orderType)) {
                    return
                }

                getNotes(notesEvent.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteUseCases.deleteNoteUseCase(notesEvent.note)
                    lastDeletedNote = notesEvent.note
                }
            }

            NotesEvent.RestoreNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteUseCases.addNoteUseCase(lastDeletedNote ?: return@launch)
                    lastDeletedNote = null
                }
            }

            NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.SearchNote -> {
                viewModelScope.launch {
                    noteUseCases.searchNotesUseCase(
                        query = notesEvent.query,
                        _state = _state,
                        originalNotes = originalNotes
                    )
                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
//        noteUseCases.getNotesUseCase(noteOrder) // It will return the flow

        getNotesJob?.cancel()

        getNotesJob = viewModelScope.launch {
            val notes =
                noteUseCases.getNotesUseCase(
                    noteOrder = noteOrder,
                    isTrashed = false,
                    isArchived = false
                ).collect {
                    originalNotes.clear()
                    originalNotes.addAll(it)

                    _state.value = state.value.copy(
                        notes = it, // It will update the notes list
                        noteOrder = noteOrder
                    )
                }
        }
    }
}
