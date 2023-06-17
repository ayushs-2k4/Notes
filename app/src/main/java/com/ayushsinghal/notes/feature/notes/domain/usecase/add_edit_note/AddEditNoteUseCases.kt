package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

data class AddEditNoteUseCases(
    val deleteNoteAddEditUseCase: DeleteNoteAddEditUseCase,
    val getNoteUseCase: GetNoteUseCase,
    val shareNoteUseCase: ShareNoteAddEditUseCase,
    val onChipClickAddEditUseCase: OnChipClickAddEditUseCase
)