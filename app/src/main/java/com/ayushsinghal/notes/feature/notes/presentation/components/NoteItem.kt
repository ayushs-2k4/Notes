package com.ayushsinghal.notes.feature.notes.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayushsinghal.notes.feature.notes.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    onDeleteClick:()->Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier)
    {
        Card(
            modifier= Modifier.fillMaxSize()
        ) {
            Column() {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = note.createdDate.toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun NoteItemPreview() {
    NoteItem(Note(title = "Title", content = "Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content ", lastModifiedDate = 2, createdDate = 1), onDeleteClick = {})
}