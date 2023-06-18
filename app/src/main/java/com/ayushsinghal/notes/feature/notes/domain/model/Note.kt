package com.ayushsinghal.notes.feature.notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "notes")
@TypeConverters(MyTypeConverters::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val content: String,
    val tags: List<String>,
    val lastModifiedDate: Long,
    val createdDate: Long,
    val isTrashed: Boolean = false
)

class InvalidNoteException(message: String) : Exception(message)