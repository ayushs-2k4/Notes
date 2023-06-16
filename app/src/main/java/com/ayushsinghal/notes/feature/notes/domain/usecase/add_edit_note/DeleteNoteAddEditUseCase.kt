package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class DeleteNoteAddEditUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.deleteNote(id)
    }
}