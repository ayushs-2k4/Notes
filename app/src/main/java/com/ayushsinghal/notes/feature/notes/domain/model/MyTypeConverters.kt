package com.ayushsinghal.notes.feature.notes.domain.model

import android.util.Log
import androidx.room.TypeConverter
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG

class MyTypeConverters {

    @TypeConverter
    fun fromListToString(value: List<String>): String {
//        Log.d(TAG, "fromListToString Length: ${(value.joinToString(",")).length}")
//        Log.d(TAG, "fromListToString String: ${(value.joinToString(","))}")
        return value.joinToString(",")
    }

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
//        val myList = value.split(",")
//        Log.d(TAG, "fromStringToList size: ${myList.size}")
//        for (item in myList) {
//            Log.d(TAG, "fromStringToList item: $item")
//        }
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",")
        }
    }
}