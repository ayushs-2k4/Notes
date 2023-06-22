package com.ayushsinghal.notes.feature.notes.domain.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ayushsinghal.notes.R
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
    val selectedColorIndex: Int = 0,
    val selectedBackgroundImageIndex: Int = 0
) {
    companion object {

        @Composable
        fun getColors(): List<Color> {

            val lightNoteColors = listOf(
                MaterialTheme.colorScheme.surface,
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

        @Composable
        fun getBackgroundImages(): List<Int> {
            val lightColorImages = listOf(
                R.drawable.image_not_supported,
                R.drawable.note_background_light_grocery_0609,
                R.drawable.note_background_light_food_0609,
                R.drawable.note_background_light_music_0609,
                R.drawable.note_background_light_travel_0614,
                R.drawable.note_background_light_recipe_0609,
                R.drawable.note_background_light_notes_0609,
                R.drawable.note_background_light_places_0609,
                R.drawable.note_background_light_video_0609,
                R.drawable.note_background_light_celebration_0714
            )

            val darkColorImages = listOf(
                R.drawable.image_not_supported,
                R.drawable.note_background_dark_grocery_0609,
                R.drawable.note_background_dark_food_0609,
                R.drawable.note_background_dark_music_0609,
                R.drawable.note_background_dark_travel_0609,
                R.drawable.note_background_dark_recipe_0609,
                R.drawable.note_background_dark_notes_0714,
                R.drawable.note_background_dark_places_0609,
                R.drawable.note_background_dark_video_0609,
                R.drawable.note_background_dark_celebration_0714
            )

            return if (isSystemInDarkTheme()) {
                darkColorImages
            } else {
                lightColorImages
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