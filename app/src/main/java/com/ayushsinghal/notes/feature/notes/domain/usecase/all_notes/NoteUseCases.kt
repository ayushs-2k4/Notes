package com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes

// So that we do not have to pass all use cases, instead we will pass this class which contains all use cases for a single feature --> We will inject this into our viewModel
data class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase
)