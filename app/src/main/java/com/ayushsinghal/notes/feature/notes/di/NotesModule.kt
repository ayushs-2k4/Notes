package com.ayushsinghal.notes.feature.notes.di

import android.app.Application
import androidx.room.Room
import com.ayushsinghal.notes.feature.notes.data.local.NoteDatabase
import com.ayushsinghal.notes.feature.notes.data.local.NoteDatabase.Companion.DATABASE_NAME
import com.ayushsinghal.notes.feature.notes.data.repository.NoteRepositoryImpl
import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.AddEditNoteUseCases
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.DeleteNoteAddEditUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.GetNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note.ShareNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.AddNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.DeleteNoteUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.GetNotesUseCase
import com.ayushsinghal.notes.feature.notes.domain.usecase.all_notes.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NotesModule {
    @Singleton
    @Provides
    fun provideNoteDatabase(app: Application): NoteDatabase {
        val noteDatabase = Room
            .databaseBuilder(
                app,
                NoteDatabase::class.java,
                DATABASE_NAME
            )
            .build()

        return noteDatabase
    }

    @Singleton
    @Provides
    fun provideNoteRepository(noteDatabase: NoteDatabase): NoteRepository { // so that we can change NoteRepository from "NoteRepositoryImpl" to something else if we want to test it or change it
        val noteRepositoryImpl = NoteRepositoryImpl(noteDatabase.noteDao)
        return noteRepositoryImpl
    }

    @Singleton
    @Provides
    fun provideUseCases(noteRepository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            addNoteUseCase = AddNoteUseCase(noteRepository),
            deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
            getNotesUseCase = GetNotesUseCase(noteRepository)
        )
    }

    @Singleton
    @Provides
    fun provideGetNoteUseCase(noteRepository: NoteRepository): GetNoteUseCase {
        return GetNoteUseCase(noteRepository)
    }

    @Singleton
    @Provides
    fun provideAddEditNoteUseCases(noteRepository: NoteRepository): AddEditNoteUseCases {
        return AddEditNoteUseCases(
            deleteNoteAddEditUseCase = DeleteNoteAddEditUseCase(noteRepository),
            getNoteUseCase = GetNoteUseCase(noteRepository),
            shareNoteUseCase = ShareNoteUseCase()
        )
    }
}