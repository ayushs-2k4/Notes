package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import android.content.Context
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.ayushsinghal.notes.feature.notes.domain.model.Note

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()

    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()

    data class EnteredContent(val value: String) : AddEditNoteEvent()

    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()

    object SaveNote : AddEditNoteEvent()

//    data class DeleteNote(val context: Context, val navController: NavController) :
//        AddEditNoteEvent()

    data class ShareNote(val context: Context) : AddEditNoteEvent()

    data class OnChipClick(val type: String, val index: Int, val tag: String) : AddEditNoteEvent()
    class OnPlusTagButtonClick(val tag: String) : AddEditNoteEvent()

    object MakeACopy : AddEditNoteEvent()

    class ChangeColor(val noteColorIndex: Int) : AddEditNoteEvent()

    class ChangeBackground(val noteBackgroundImageIndex: Int) : AddEditNoteEvent()
}