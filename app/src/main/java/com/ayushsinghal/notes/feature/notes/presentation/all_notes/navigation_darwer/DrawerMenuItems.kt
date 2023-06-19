package com.ayushsinghal.notes.feature.notes.presentation.all_notes.navigation_darwer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import com.ayushsinghal.notes.R
import com.ayushsinghal.notes.util.NavigationDrawerScreen
import com.ayushsinghal.notes.util.Screen

data class DrawerMenuItem(
    val icon: ImageVector,
    val label: String,
    val navigationDrawerScreen: String
)

object DrawerMenuItems {

    val navigationDrawerItems = listOf(
        DrawerMenuItem(
            icon = Icons.Outlined.Lightbulb,
            label = "Notes",
            navigationDrawerScreen = Screen.AllNotesScreen.route
        ),
        DrawerMenuItem(
            icon = Icons.Outlined.Archive,
            label = "Archive",
            navigationDrawerScreen = NavigationDrawerScreen.Archive.route
        ),
        DrawerMenuItem(
            icon = Icons.Outlined.Delete,
            label = "Trash",
            navigationDrawerScreen = NavigationDrawerScreen.Trash.route
        ),
        DrawerMenuItem(
            icon = Icons.Outlined.Settings,
            label = "Settings",
            navigationDrawerScreen = NavigationDrawerScreen.Settings.route
        ),
        DrawerMenuItem(
            icon = Icons.Outlined.Feedback,
            label = "Feedback",
            navigationDrawerScreen = NavigationDrawerScreen.Feedback.route
        )
    )
}