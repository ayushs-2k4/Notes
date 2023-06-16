package com.ayushsinghal.notes.feature.notes.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.NoteUseCases
import com.ayushsinghal.notes.feature.notes.util.NoteOrder
import com.ayushsinghal.notes.feature.notes.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf<NotesState>(NotesState())
    val state: State<NotesState> = _state

    private var lastDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.LastModifiedDate(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                // Checking if order is changed
                if ((_state.value.noteOrder::class == event.noteOrder::class) && (_state.value.noteOrder.orderType == event.noteOrder.orderType)) {
                    return
                }

                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteUseCases.deleteNoteUseCase(event.note)
                    lastDeletedNote = event.note
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
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
//        noteUseCases.getNotesUseCase(noteOrder) // It will return the flow

        getNotesJob?.cancel()

        getNotesJob = noteUseCases.getNotesUseCase(noteOrder).onEach {
            _state.value = state.value.copy(
                notes = it, // It will update the notes list
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }
}