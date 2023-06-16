package com.ayushsinghal.notes.feature.notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ayushsinghal.notes.feature.notes.domain.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    //    abstract fun getNoteDao(): NoteDao
    abstract val noteDao: NoteDao

    companion object {

//        private var INSTANCE: NoteDatabase? = null

//        fun getNoteDatabase(context: Context): NoteDatabase {
//            if (INSTANCE == null) {
//                INSTANCE =
//                    Room.databaseBuilder(context, NoteDatabase::class.java, "note_database").build()
//            }
//            return INSTANCE!!
//        }

        const val DATABASE_NAME = "notes_db"
    }
}