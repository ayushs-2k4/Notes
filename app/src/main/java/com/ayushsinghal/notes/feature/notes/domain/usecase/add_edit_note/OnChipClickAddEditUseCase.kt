package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

import android.util.Log
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.Note

class OnChipClickAddEditUseCase {

    suspend operator fun invoke(id: Int) {
        Log.d(TAG, "id: $id")
    }
}