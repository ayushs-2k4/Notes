package com.ayushsinghal.notes.feature.notes.presentation.all_notes

import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.util.NoteOrder

// Things a User can do, like changing order, deleting note, etc
sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}