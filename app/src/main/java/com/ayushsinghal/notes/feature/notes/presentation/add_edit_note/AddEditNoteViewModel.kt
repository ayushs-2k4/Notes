package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.InvalidNoteException
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.AddEditNoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _tagsLiveData = MutableStateFlow<List<String>>(emptyList())
    val tagsLiveData: Flow<List<String>> = _tagsLiveData

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

                        _tagsLiveData.value = note?.tags ?: emptyList()
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
                                tags = _tagsLiveData.value,
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
                            tags = emptyList(),
                            lastModifiedDate = 1,
                            createdDate = 1
                        )
                    )
                }
            }

            is AddEditNoteEvent.OnChipClick -> { // If clicked on existing chip
                if (addEditNoteEvent.type == "Update") {
                    Log.d(TAG, "NoteID: $currentNoteId")
                    Log.d(TAG, "index: ${addEditNoteEvent.index}")
                    if ((addEditNoteEvent.tag.isNotEmpty())) {
                        val updatedList = _tagsLiveData.value.mapIndexed { index, value ->
                            if (index == addEditNoteEvent.index) {
                                // Update the value at the target index
                                // You can modify the value here as per your requirements
                                addEditNoteEvent.tag
                            } else {
                                value
                            }
                        }
                        _tagsLiveData.value = updatedList
                        Log.d(TAG, "attested: ${_tagsLiveData.value[addEditNoteEvent.index]}")
                    }
                } else if (addEditNoteEvent.type == "Delete") {
                    val updatedList = _tagsLiveData.value.filterIndexed { index, _ ->
                        index != addEditNoteEvent.index
                    }
                    _tagsLiveData.value = updatedList
                }
            }

            is AddEditNoteEvent.OnPlusTagButtonClick -> { // If clicked on plus button to add new tag
                Log.d(TAG, "NoteID: $currentNoteId")
                if (addEditNoteEvent.tag.isNotEmpty()) {
                    _tagsLiveData.value = _tagsLiveData.value + addEditNoteEvent.tag
                }
            }
        }
    }

    private fun hasNoteContentChanged(oldNoteTitle: String, oldNoteContent: String): Boolean {
        return !((oldNoteTitle == _noteTitle.value.text) && (oldNoteContent == _noteContent.value.text))
    }

}