package com.ayushsinghal.notes.feature.notes.presentation.all_notes

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.ayushsinghal.notes.feature.notes.util.NoteOrder
import com.ayushsinghal.notes.feature.notes.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val noteRepository: NoteRepository
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

                if (notesEvent.query.isNotEmpty()) {
                    _state.value = _state.value.copy(notes = originalNotes)

                    val filteredNotes = _state.value.notes.filter { note ->
                        note.title.contains(notesEvent.query, ignoreCase = true) ||
                                note.content.contains(notesEvent.query, ignoreCase = true) ||
                                note.tags.any { tag ->
                                    tag.contains(
                                        notesEvent.query,
                                        ignoreCase = true
                                    )
                                }
                    }

                    _state.value = state.value.copy(notes = filteredNotes)
                } else {
                    _state.value = _state.value.copy(notes = originalNotes)
                }

//                Log.d(TAG, "Nope")
//                if (notesEvent.query.isNotEmpty()) {
//                    Log.d(TAG, "Yep")
//                    viewModelScope.launch {
//                        Log.d(TAG, "Nope")
//                        _state.value =
//                            _state.value.copy(notes = noteUseCases.searchNotesUseCase(notesEvent.query))
//                    }
//                } else {
//                    _state.value = _state.value.copy(notes = originalNotes)
//                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
//        noteUseCases.getNotesUseCase(noteOrder) // It will return the flow

        getNotesJob?.cancel()

        getNotesJob = viewModelScope.launch {
            val notes = noteUseCases.getNotesUseCase(noteOrder = noteOrder).collect {
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
