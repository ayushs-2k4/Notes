package com.ayushsinghal.notes.feature.notes.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("DELETE FROM notes")
    suspend fun clearNotes()

    @Query("UPDATE notes SET isTrashed = 1 WHERE id = :id")
    suspend fun moveNoteToTrash(id: Int)

    @Query("UPDATE notes SET isTrashed = 0 WHERE id = :id")
    suspend fun restoreNoteFromTrash(id: Int)

    @Query("SELECT * FROM notes WHERE isTrashed = 1")
    fun getTrashedNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isTrashed = 0")
    fun getNonTrashedNotes(): Flow<List<Note>>

}