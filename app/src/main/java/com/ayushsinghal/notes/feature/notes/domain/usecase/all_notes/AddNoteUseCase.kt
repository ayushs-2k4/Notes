package com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes

import com.ayushsinghal.notes.feature.notes.domain.model.InvalidNoteException
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository

class AddNoteUseCase(
    private val noteRepository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isEmpty() && note.content.isEmpty()) {
            throw InvalidNoteException("Both Title and content can not be empty")
        } else {
            noteRepository.upsertNote(note)
        }
    }
}