package com.ayushsinghal.notes.feature.notes.util

sealed class NoteStatus(val type:String) {
    object NewNote:NoteStatus("NewNote")

    object ExistingNote:NoteStatus("ExistingNote")

    object TrashedNote:NoteStatus("TrashedNote")
}