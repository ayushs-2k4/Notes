package com.ayushsinghal.notes.feature.notes.presentation

import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.util.NoteOrder
import com.ayushsinghal.notes.feature.notes.util.OrderType

// Class for states in which our UI can be
data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.LastModifiedDate(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)