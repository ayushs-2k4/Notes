package com.ayushsinghal.notes.feature.notes.domain.repository

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("DELETE FROM notes")
    suspend fun clearNotes()
}