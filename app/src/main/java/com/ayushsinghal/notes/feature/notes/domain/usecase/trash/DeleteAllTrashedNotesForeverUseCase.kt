package com.ayushsinghal.notes.feature.notes.domain.usecase.trash

import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class DeleteAllTrashedNotesForeverUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke() {
        repository.deleteAllTrashedNotes()
    }
}