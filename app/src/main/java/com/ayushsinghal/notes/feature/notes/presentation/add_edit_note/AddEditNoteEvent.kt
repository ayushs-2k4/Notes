package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import android.content.Context
import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()

    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()

    data class EnteredContent(val value: String) : AddEditNoteEvent()

    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()

    object SaveNote : AddEditNoteEvent()

    object DeleteNote : AddEditNoteEvent()

    data class ShareNote(val context: Context) : AddEditNoteEvent()
}