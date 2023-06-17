package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

import android.content.Context
import android.content.Intent
import com.ayushsinghal.notes.feature.notes.domain.model.Note

class ShareNoteUseCase {

    suspend operator fun invoke(context: Context, note: Note) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT, note.title + "\n" + "\n" + note.content
        )
        intent.type = "text/plain"
        val intentChooser = Intent.createChooser(intent, "Hi")
        context.startActivity(intentChooser)
    }
}