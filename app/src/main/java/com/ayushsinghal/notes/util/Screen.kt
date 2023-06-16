package com.ayushsinghal.notes.util

sealed class Screen(val route: String) {
    object AllNotesScreen : Screen("AllNotesScreen")
    object AddEditNoteScreen : Screen("AddEditNoteScreen")
}