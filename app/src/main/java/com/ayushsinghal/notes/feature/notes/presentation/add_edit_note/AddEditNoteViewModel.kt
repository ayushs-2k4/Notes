package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.InvalidNoteException
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.AddEditNoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.archive.ArchiveUseCases
import com.ayushsinghal.notes.feature.notes.util.NoteStatus
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
//    private val archiveUseCases: ArchiveUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Here we are not taking complete screen as one state just like in All Notes Screen, because whenever the text of title ot content or anything will change, the whole screen will be re-composed
    private val _noteTitle =
        mutableStateOf<NoteTextFieldState>(NoteTextFieldState(hint = "Title"))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent =
        mutableStateOf<NoteTextFieldState>(NoteTextFieldState(hint = "Note"))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _currentNoteCreatedDate = mutableStateOf<Long>(System.currentTimeMillis())
    val currentNoteCreatedDate: State<Long> = _currentNoteCreatedDate

    private val _currentNoteLastModifiedDate = mutableStateOf<Long>(System.currentTimeMillis())
    val currentNoteLastModifiedDate: State<Long> = _currentNoteLastModifiedDate

    private val _tagsLiveData = MutableStateFlow<List<String>>(emptyList())
    val tagsLiveData: Flow<List<String>> = _tagsLiveData

    private val _noteColorIndex = mutableStateOf<Int>(0)
    val noteColorIndex: State<Int> = _noteColorIndex


    var currentNoteId: Int? = null

    private var oldNoteTitle: String? = null
    private var oldNoteContent: String? = null
    private var oldNoteTagsList: List<String>? = null

//    private var isTrashed: Boolean = false

    //    var noteStatus: String? = null
    lateinit var noteStatus: String


    init {
        savedStateHandle.get<Int>("noteId")
            ?.let { noteId -> // If we clicked on a current Note to edit it
                if (noteId != -1) { // Because New Note will have initial id as -1
                    viewModelScope.launch {
                        addEditNoteUseCases.getNoteUseCase(noteId)?.also { note ->
                            currentNoteId = note.id
                            _noteTitle.value = noteTitle.value.copy(
                                text = note.title,
                            )
                            _noteContent.value = _noteContent.value.copy(
                                text = note.content,
                            )
                        }
                    }


                    viewModelScope.launch {

                        val note: Note? = addEditNoteUseCases.getNoteUseCase(noteId)

                        _currentNoteCreatedDate.value =
                            note?.createdDate ?: System.currentTimeMillis()
                        _currentNoteLastModifiedDate.value =
                            note?.lastModifiedDate ?: System.currentTimeMillis()

                        oldNoteTitle = note?.title
                        oldNoteContent = note?.content

                        oldNoteTagsList = note?.tags

                        _tagsLiveData.value = note?.tags ?: emptyList()

//                        isTrashed = note?.isTrashed!!

                        _noteColorIndex.value = (note?.selectedColorIndex!!)

                    }
                }
            }

        savedStateHandle.get<String>("noteStatus")?.let {
            noteStatus = it
        }
    }

    fun onEvent(addEditNoteEvent: AddEditNoteEvent) {
        when (addEditNoteEvent) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = addEditNoteEvent.value,
                )
            }


            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = addEditNoteEvent.value,
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                )
            }

            AddEditNoteEvent.SaveNote -> {
                if (noteStatus != NoteStatus.TrashedNote.type) {
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            noteUseCases.addNoteUseCase(
                                Note(
                                    id = currentNoteId,
                                    title = _noteTitle.value.text,
                                    content = _noteContent.value.text,
                                    tags = _tagsLiveData.value,
                                    createdDate = currentNoteCreatedDate.value
                                        ?: System.currentTimeMillis(),
                                    lastModifiedDate =
                                    if (currentNoteId == null) {
                                        System.currentTimeMillis()
                                    } else {
                                        if (hasNoteContentChanged(
                                                oldNoteTitle = oldNoteTitle!!,
                                                oldNoteContent = oldNoteContent!!,
                                                oldNoteTagsList = oldNoteTagsList!!
                                            )
                                        ) {
                                            System.currentTimeMillis()
                                        } else {
                                            currentNoteLastModifiedDate.value
                                        }
                                    },
                                    isArchived = noteStatus == NoteStatus.ArchivedNote.type,
                                    selectedColorIndex = noteColorIndex.value
                                )
                            )
                        } catch (e: InvalidNoteException) {
                            // Show Snackbar
                        }
                    }
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
                viewModelScope.launch {
                    addEditNoteUseCases.onChipClickAddEditUseCase(
                        type = addEditNoteEvent.type,
                        index = addEditNoteEvent.index,
                        _tagsLiveData = _tagsLiveData,
                        tag = addEditNoteEvent.tag
                    )
                }
            }

            is AddEditNoteEvent.OnPlusTagButtonClick -> { // If clicked on plus button to add new tag
                viewModelScope.launch {
                    addEditNoteUseCases.onPlusTagButtonClickAddEditUseCase(
                        _tagsLiveData = _tagsLiveData,
                        tag = addEditNoteEvent.tag
                    )
                }
            }

            is AddEditNoteEvent.MakeACopy -> {
                viewModelScope.launch {
                    addEditNoteUseCases.makeACopyAddEditUseCase(
                        noteUseCases = noteUseCases,
                        note = Note(
                            title = _noteTitle.value.text,
                            content = _noteContent.value.text,
                            tags = _tagsLiveData.value,
                            createdDate = System.currentTimeMillis(),
                            lastModifiedDate = System.currentTimeMillis()
                        )
                    )
                }
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColorIndex.value = addEditNoteEvent.noteColorIndex
            }
        }
    }

    private fun hasNoteContentChanged(
        oldNoteTitle: String,
        oldNoteContent: String,
        oldNoteTagsList: List<String>
    ): Boolean {
        return !((oldNoteTitle == _noteTitle.value.text) && (oldNoteContent == _noteContent.value.text) && (oldNoteTagsList == _tagsLiveData.value))
    }

}