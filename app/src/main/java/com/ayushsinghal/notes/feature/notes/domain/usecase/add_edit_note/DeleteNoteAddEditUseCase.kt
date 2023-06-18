package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.navigation.NavController
import com.ayushsinghal.notes.feature.notes.domain.repository.NoteRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DeleteNoteAddEditUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(
        context: Context,
        viewModelScope: CoroutineScope,
        navController: NavController,
        id: Int
    ) {
//        repository.deleteNote(id)
        repository.moveNoteToTrash(id)
    }
}