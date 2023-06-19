package com.ayushsinghal.notes.feature.notes.data.repository

import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.data.local.NoteDao
import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun upsertNote(note: Note) {
        noteDao.upsertNote(note)
    }

    override suspend fun deleteNote(id: Int) {
        noteDao.deleteNote(id)
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override fun getMainNotes(): Flow<List<Note>> {
        return noteDao.getMainNotes()
    }

    override fun getNonTrashedNotes(): Flow<List<Note>> {
        return noteDao.getNonTrashedNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun clearNotes() {
        noteDao.clearNotes()
    }

    override suspend fun moveNoteToTrash(id: Int) {
        noteDao.moveNoteToTrash(id)
    }

    override suspend fun restoreNoteFromTrash(id: Int) {
        noteDao.restoreNoteFromTrash(id)
    }

    override fun getTrashedNotes(): Flow<List<Note>> {
        return noteDao.getTrashedNotes()
    }

    override suspend fun deleteAllTrashedNotes() {
        noteDao.deleteAllTrashedNotes()
    }

    override suspend fun archiveNote(id: Int) {
        noteDao.archiveNote(id)
    }

    override suspend fun unArchiveNote(id: Int) {
        noteDao.unArchiveNote(id)
    }

    override fun getArchivedNotes(): Flow<List<Note>> {
        return noteDao.getArchivedNotes()
    }

    override fun getNonArchivedNotes(): Flow<List<Note>> {
        return noteDao.getNonArchivedNotes()
    }

}