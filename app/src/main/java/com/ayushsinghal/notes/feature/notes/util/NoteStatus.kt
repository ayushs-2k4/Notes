package com.ayushsinghal.notes.feature.notes.util

sealed class NoteStatus(val type: String) {
    object NewNote : NoteStatus(type = "NewNote")

    object ExistingNote : NoteStatus(type = "ExistingNote")

    object TrashedNote : NoteStatus(type = "TrashedNote")

    object ArchivedNote : NoteStatus(type = "ArchivedNote")
}