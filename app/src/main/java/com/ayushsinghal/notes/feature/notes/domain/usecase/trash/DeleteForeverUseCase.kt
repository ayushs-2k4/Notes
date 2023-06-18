package com.ayushsinghal.notes.feature.notes.domain.usecase.trash

import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class DeleteForeverUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.deleteNote(id)
    }
}