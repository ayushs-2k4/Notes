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
//        val builder = MaterialAlertDialogBuilder(context)
//        builder.setTitle("Confirm Delete")
//        builder.setMessage("Do you want to delete this note?")
//        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
//            viewModelScope.launch {
//                repository.deleteNote(id)
//            }
//            dialog.cancel()
//        })
//
//        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
//            dialog.cancel()
//        })
//        val alert = builder.create()
//        alert.show()

        repository.deleteNote(id)
//        navController.navigateUp()

//        showDialogConfirmation(context = context, onConfirm = {
//            viewModelScope.launch {
//                repository.deleteNote(id)
//                navController.popBackStack()
//                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

//    private suspend fun showDialogConfirmation(context: Context, onConfirm: () -> Unit) {
//        // Display the confirmation dialog using your preferred dialog library
//        // For example, using MaterialAlertDialogBuilder from the Material Components library
//        val builder = MaterialAlertDialogBuilder(context)
//        builder.setTitle("Confirm Delete")
//        builder.setMessage("Do you want to delete this note?")
//        builder.setPositiveButton("Yes") { dialog, _ ->
//            onConfirm()
//            dialog.dismiss()
//        }
//        builder.setNegativeButton("No") { dialog, _ ->
//            dialog.dismiss()
//        }
//        builder.show()
//    }
}