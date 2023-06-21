package com.ayushsinghal.notes.feature.notes.domain.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ayushsinghal.notes.ui.theme.LightYellow
import com.ayushsinghal.notes.ui.theme.Lilac
import com.ayushsinghal.notes.ui.theme.MintGreen
import com.ayushsinghal.notes.ui.theme.PalePink
import com.ayushsinghal.notes.ui.theme.PaleTurquoise
import com.ayushsinghal.notes.ui.theme.PastelGreen
import com.ayushsinghal.notes.ui.theme.Peach
import com.ayushsinghal.notes.ui.theme.SkyBlue
import com.ayushsinghal.notes.ui.theme.SoftCoral
import com.ayushsinghal.notes.ui.theme.SoftLavender

@Entity(tableName = "notes")
@TypeConverters(MyTypeConverters::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val content: String,
    val tags: List<String>,
    val lastModifiedDate: Long,
    val createdDate: Long,
    val isArchived: Boolean = false,
    val isTrashed: Boolean = false,
    val selectedColorIndex: Int = 0
) {
    companion object {

//        val noteColors = getColors()


        @Composable
        fun getColors(): List<Color> {

            val lightNoteColors = listOf(
                MaterialTheme.colorScheme.surface,
                Color.Red,
                Color.Cyan,
                PalePink,
                SoftLavender,
                MintGreen,
                SkyBlue,
                Peach,
                Lilac,
                LightYellow,
                PaleTurquoise,
                SoftCoral,
                PastelGreen
            )

            val darkNoteColors = lightNoteColors.map {
                if (it != MaterialTheme.colorScheme.surface) {
                    it.darken()
                } else {
                    it
                }
            }

            return if (isSystemInDarkTheme()) {
                darkNoteColors
            } else {
                lightNoteColors
            }
        }

        private fun Color.darken(): Color {
            val darkFactor = 0.35f // Adjust this factor to control darkness
            val red = (red * darkFactor).coerceIn(0f, 1f)
            val green = (green * darkFactor).coerceIn(0f, 1f)
            val blue = (blue * darkFactor).coerceIn(0f, 1f)
            return Color(red, green, blue)
        }
    }
}

class InvalidNoteException(message: String) : Exception(message)