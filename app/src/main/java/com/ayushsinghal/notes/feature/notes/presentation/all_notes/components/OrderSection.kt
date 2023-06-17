package com.ayushsinghal.notes.feature.notes.presentation.all_notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ayushsinghal.notes.feature.notes.util.NoteOrder
import com.ayushsinghal.notes.feature.notes.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.LastModifiedDate(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title,
                onCheck = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) })

            DefaultRadioButton(
                text = "Last Modified Date",
                selected = noteOrder is NoteOrder.LastModifiedDate,
                onCheck = { onOrderChange(NoteOrder.LastModifiedDate(noteOrder.orderType)) })

            DefaultRadioButton(
                text = "Created Date",
                selected = noteOrder is NoteOrder.CreatedDate,
                onCheck = { onOrderChange(NoteOrder.CreatedDate(noteOrder.orderType)) })
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onCheck = { onOrderChange(noteOrder.copy(OrderType.Ascending)) })

            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onCheck = { onOrderChange(noteOrder.copy(OrderType.Descending)) })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OrderSectionPreview() {
    OrderSection(onOrderChange = {})
}