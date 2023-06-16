package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.notes.domain.model.InvalidNoteException
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.AddEditNoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.GetNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val addEditNoteUseCases: AddEditNoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Here we are not taking complete screen as one state just like in All Notes Screen, because whenever the text of title ot content or anything will change, the whole screen will be re-composed
    private val _noteTitle = mutableStateOf<NoteTextFieldState>(NoteTextFieldState(hint = "Title"))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf<NoteTextFieldState>(NoteTextFieldState(hint = "Note"))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")
            ?.let { noteId -> // If we clicked on a current Note to edit it
                if (noteId != -1) { // Because New Note will have initial id as -1
                    viewModelScope.launch(Dispatchers.IO) {
                        addEditNoteUseCases.getNoteUseCase(noteId)?.also { note ->
                            currentNoteId = noteId
                            _noteTitle.value = noteTitle.value.copy(
                                text = note.title,
                                isHintVisible = false
                            )

                            _noteContent.value = noteContent.value.copy(
                                text = note.content,
                                isHintVisible = false
                            )
                        }
                    }
                }
            }
    }

    fun onEvent(addEditNoteEvent: AddEditNoteEvent) {
        when (addEditNoteEvent) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = addEditNoteEvent.value
                )
            }


            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
                    isHintVisible = (!addEditNoteEvent.focusState.isFocused) && (_noteTitle.value.text.isBlank())
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = addEditNoteEvent.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = (!addEditNoteEvent.focusState.isFocused) && (_noteContent.value.text.isBlank())
                )
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                id = currentNoteId,
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                createdDate = System.currentTimeMillis(),
                                lastModifiedDate = System.currentTimeMillis()
                            )
                        )
                    } catch (e: InvalidNoteException) {
                        // Show Snackbar
                    }
                }
            }

            AddEditNoteEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addEditNoteUseCases.deleteNoteAddEditUseCase(currentNoteId!!)
                }
            }
        }
    }

}