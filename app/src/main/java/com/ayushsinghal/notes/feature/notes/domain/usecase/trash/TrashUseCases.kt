package com.ayushsinghal.notes.feature.notes.domain.usecase.trash

data class TrashUseCases(
    val moveNoteToTrashUseCase: MoveNoteToTrashUseCase,
    val deleteForeverUseCase: DeleteForeverUseCase,
    val restoreNoteUseCase: RestoreNoteUseCase,
    val deleteAllTrashedNotesForeverUseCase: DeleteAllTrashedNotesForeverUseCase
)