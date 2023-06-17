package com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes

import android.util.Log
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.GetNoteUseCase
import kotlinx.coroutines.flow.collect

class SearchNotesUseCase(
    private val noteRepository: NoteRepository,
) {

    suspend operator fun invoke(query: String): List<Note> {
//        return if (query.isNotEmpty()) {
//            notes.filter { note ->
//                note.title.contains(query, ignoreCase = true) ||
//                        note.content.contains(query, ignoreCase = true) ||
//                        note.tags.any { tag ->
//                            tag.contains(query, ignoreCase = true)
//                        }
//            }
//        } else {
//            notes
//        }

        val ansList = mutableListOf<Note>()

        Log.d(TAG, "In SearchNotesUseCase")

        try {

            noteRepository.getAllNotes().collect { listOfNotes ->
                listOfNotes.forEach { note ->
//                    if ((note.title.contains(query, ignoreCase = true)) ||
//                        (note.content.contains(query, ignoreCase = true)) ||
//                        (note.tags.any { tag ->
//                            tag.contains(query, ignoreCase = true)
//                        })
//                    ) {
//                        Log.d(TAG, "Adding to ansList")
//                        ansList.add(note)
//                    }

                    if (note.title.contains(query, ignoreCase = true)
                    ) {
                        Log.d(TAG, "Adding to ansList")
                        ansList.add(note)
                    }
                }
            }

            Log.d(TAG, "In SearchNotesUseCase --> ansList.size: ${ansList.size}")
        } catch (e: Exception) {
            Log.e(TAG, "Exception occurred: ${e.message}")
            e.printStackTrace()
        }

        return ansList
    }
}