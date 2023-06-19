package com.ayushsinghal.notes.feature.notes.domain.usecase.trash

import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class MoveNoteToTrashUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.moveNoteToTrash(id)
    }

}