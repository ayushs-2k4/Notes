package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen

sealed class TrashEvent {

    data class MoveNoteToTrash(val id: Int) : TrashEvent()

    data class DeleteNoteForever(val id: Int) : TrashEvent()

    data class RestoreNote(val id: Int) : TrashEvent()

    object DeleteAllTrashedNotesForever : TrashEvent()
}