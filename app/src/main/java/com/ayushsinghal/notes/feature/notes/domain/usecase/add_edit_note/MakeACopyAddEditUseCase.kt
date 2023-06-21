package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases

class MakeACopyAddEditUseCase {

    suspend operator fun invoke(noteUseCases: NoteUseCases, note: Note) {
        noteUseCases.addNoteUseCase(note = note)
    }
}