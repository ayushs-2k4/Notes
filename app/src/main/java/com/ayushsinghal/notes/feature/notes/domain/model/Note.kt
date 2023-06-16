package com.ayushsinghal.notes.feature.notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val content: String,
    val lastModifiedDate: Long,
    val createdDate: Long
)

class InvalidNoteException(message: String) : Exception(message)