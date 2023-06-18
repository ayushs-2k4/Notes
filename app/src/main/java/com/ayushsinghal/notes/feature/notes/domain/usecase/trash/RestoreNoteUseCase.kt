package com.ayushsinghal.notes.feature.notes.domain.usecase.trash

import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class RestoreNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.restoreNoteFromTrash(id)
    }
}