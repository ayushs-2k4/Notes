package com.ayushsinghal.notes.feature.notes.domain.usecase.archive

import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class UnArchiveNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.unArchiveNote(id)
    }
}