package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

data class AddEditNoteUseCases(
    val getNoteUseCase: GetNoteUseCase,
    val shareNoteUseCase: ShareNoteAddEditUseCase,
    val onChipClickAddEditUseCase: OnChipClickAddEditUseCase,
    val onPlusTagButtonClickAddEditUseCase: OnPlusTagButtonClickAddEditUseCase,
    val makeACopyAddEditUseCase: MakeACopyAddEditUseCase
)