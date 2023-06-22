package com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes

import androidx.compose.runtime.MutableState
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.NotesState

class SearchNotesUseCase {
    operator fun invoke(
        query: String,
        _state: MutableState<NotesState>,
        originalNotes: List<Note>
    ) {
        if (query.isNotEmpty()) {
            val filteredNotes = originalNotes.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true) ||
                        note.tags.any { tag ->
                            tag.contains(query, ignoreCase = true)
                        }
            }
            _state.value = _state.value.copy(notes = filteredNotes)
        } else {
            // Reset notes to original list if query is empty
            _state.value = _state.value.copy(notes = originalNotes)
        }
    }
}