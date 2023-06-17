package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import android.content.DialogInterface
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ayushsinghal.notes.R
import com.ayushsinghal.notes.feature.notes.domain.model.InvalidNoteException
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.AddEditNoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.GetNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

    private val _currentNotesCreatedDate2 = mutableStateOf<Long>(-1)
    val currentNotesCreatedDate2: State<Long> = _currentNotesCreatedDate2

    private val _currentNotesLastModifiedDate2 = mutableStateOf<Long>(-1)
    val currentNotesLastModifiedDate2: State<Long> = _currentNotesLastModifiedDate2

    private var currentNoteId: Int? = null
    private var currentNoteCreatedDate: Long? = null
    private var currentNoteLastModifiedDate: Long? = null
    private var oldNoteTitle: String? = null
    private var oldNoteContent: String? = null

    init {
        savedStateHandle.get<Int>("noteId")
            ?.let { noteId -> // If we clicked on a current Note to edit it
                if (noteId != -1) { // Because New Note will have initial id as -1
                    viewModelScope.launch {
                        addEditNoteUseCases.getNoteUseCase(noteId)?.also { note ->
                            currentNoteId = noteId
                            _noteTitle.value = noteTitle.value.copy(
//                                isHintVisible =  _noteTitle.value.text.isBlank(),
                                isHintVisible = note.title.isBlank(),
                                text = note.title,
                            )
//                                val myBool =  note.content.isBlank()

                            _noteContent.value = noteContent.value.copy(
//                                isHintVisible = _noteContent.value.text.isBlank(),
                                isHintVisible = note.content.isBlank(),
//                                isHintVisible = myBool,
                                text = note.content,
                            )
                        }
                    }

                    viewModelScope.launch {

                        val note: Note? = addEditNoteUseCases.getNoteUseCase(noteId)

                        currentNoteCreatedDate = note?.createdDate
                        currentNoteLastModifiedDate = note?.lastModifiedDate

                        _currentNotesCreatedDate2.value = note?.createdDate ?: -1
                        _currentNotesLastModifiedDate2.value = note?.lastModifiedDate ?: -1

                        oldNoteTitle = note?.title
                        oldNoteContent = note?.content

                    }
                }
            }
    }

    fun onEvent(addEditNoteEvent: AddEditNoteEvent) {
        when (addEditNoteEvent) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = _noteTitle.value.text.isBlank(),
                    text = addEditNoteEvent.value,
                )
            }


            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
//                    isHintVisible = (!addEditNoteEvent.focusState.isFocused) && (_noteTitle.value.text.isBlank())
                    isHintVisible = _noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = _noteContent.value.text.isBlank(),
                    text = addEditNoteEvent.value,
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = _noteContent.value.text.isBlank()
                )
//                    isHintVisible = (!addEditNoteEvent.focusState.isFocused) && (_noteContent.value.text.isBlank())
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                id = currentNoteId,
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                createdDate = currentNoteCreatedDate ?: System.currentTimeMillis(),
                                lastModifiedDate =
                                if (currentNoteId == null) {
                                    System.currentTimeMillis()
                                } else {
                                    if (hasNoteContentChanged(
                                            oldNoteTitle = oldNoteTitle!!,
                                            oldNoteContent = oldNoteContent!!
                                        )
                                    ) {
                                        System.currentTimeMillis()
                                    } else {
                                        currentNoteLastModifiedDate!!
                                    }
                                }
                            )
                        )
                    } catch (e: InvalidNoteException) {
                        // Show Snackbar
                    }
                }
            }

            is AddEditNoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    addEditNoteUseCases.deleteNoteAddEditUseCase(
                        context = addEditNoteEvent.context,
                        viewModelScope = viewModelScope,
                        navController = addEditNoteEvent.navController,
                        id = currentNoteId!!
                    )
                }
            }

            is AddEditNoteEvent.ShareNote -> {
                viewModelScope.launch {
                    addEditNoteUseCases.shareNoteUseCase(
                        context = addEditNoteEvent.context,
                        note = Note(
                            title = _noteTitle.value.text,
                            content = _noteContent.value.text,
                            lastModifiedDate = 1,
                            createdDate = 1
                        )
                    )
                }
            }
        }
    }

    private fun hasNoteContentChanged(oldNoteTitle: String, oldNoteContent: String): Boolean {
        if ((oldNoteTitle == _noteTitle.value.text) && (oldNoteContent == _noteContent.value.text)) {
            return false
        }
        return true
    }

}