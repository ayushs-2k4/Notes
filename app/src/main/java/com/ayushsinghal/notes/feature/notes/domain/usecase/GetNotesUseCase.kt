package com.ayushsinghal.notes.feature.notes.domain.usecase

import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository
import com.ayushsinghal.notes.feature.notes.util.NoteOrder
import com.ayushsinghal.notes.feature.notes.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val noteRepository: NoteRepository
) {

    operator fun invoke(noteOrder: NoteOrder = NoteOrder.LastModifiedDate(OrderType.Descending)): Flow<List<Note>> {
        return noteRepository.getAllNotes().map { notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> {
                            notes.sortedBy { it.title.lowercase() }
                        }

                        is NoteOrder.LastModifiedDate -> {
                            notes.sortedBy { it.lastModifiedDate }
                        }

                        is NoteOrder.CreatedDate -> {
                            notes.sortedBy { it.createdDate }
                        }
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> {
                            notes.sortedByDescending { it.title.lowercase() }
                        }

                        is NoteOrder.LastModifiedDate -> {
                            notes.sortedByDescending { it.lastModifiedDate }
                        }

                        is NoteOrder.CreatedDate -> {
                            notes.sortedByDescending { it.createdDate }
                        }
                    }
                }
            }

        }
    }
}