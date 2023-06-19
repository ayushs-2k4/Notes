package com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen

sealed class ArchiveEvent {
    data class MoveNoteToArchive(val id: Int) : ArchiveEvent()

    data class MoveNoteFromArchive(val id: Int) : ArchiveEvent()
}