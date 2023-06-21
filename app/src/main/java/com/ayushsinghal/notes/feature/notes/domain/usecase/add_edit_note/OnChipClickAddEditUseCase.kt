package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

import android.util.Log
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.MutableStateFlow

class OnChipClickAddEditUseCase {

    suspend operator fun invoke(
        type: String,
        index: Int,
        _tagsLiveData: MutableStateFlow<List<String>>,
        tag: String
    ) {
        if (type == "Update") {
            if (tag.isNotEmpty()) {
                val updatedList = _tagsLiveData.value.mapIndexed { i, value ->
                    if (i == index) {
                        tag
                    } else {
                        value
                    }
                }
                _tagsLiveData.value = updatedList
                Log.d(TAG, "attested: ${_tagsLiveData.value[index]}")
            }
        } else if (type == "Delete") {
            val updatedList = _tagsLiveData.value.filterIndexed { i, _ ->
                i != index
            }
            _tagsLiveData.value = updatedList
        }
    }
}