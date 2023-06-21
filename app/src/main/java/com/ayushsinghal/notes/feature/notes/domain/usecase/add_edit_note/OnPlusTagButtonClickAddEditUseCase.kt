package com.ayushsinghal.notes.feature.notes.domain.usecase.add_edit_note

import kotlinx.coroutines.flow.MutableStateFlow

class OnPlusTagButtonClickAddEditUseCase {

    suspend operator fun invoke(
        _tagsLiveData: MutableStateFlow<List<String>>,
        tag: String
    ) {
        if ((tag.isNotEmpty()) && (!_tagsLiveData.value.contains(
                tag
            ))
        ) {
            _tagsLiveData.value = _tagsLiveData.value + tag
        }
    }
}