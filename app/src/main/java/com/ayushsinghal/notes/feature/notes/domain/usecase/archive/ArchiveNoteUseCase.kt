package com.ayushsinghal.notes.feature.notes.domain.usecase.archive

import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class ArchiveNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.archiveNote(id)
    }
}