package com.ayushsinghal.notes.util

sealed class NavigationDrawerScreen(route: String) {
    object Archive : Screen("archive")
    object Trash : Screen("trash")
    object Settings : Screen("settings")
    object Feedback : Screen("feedback")
}