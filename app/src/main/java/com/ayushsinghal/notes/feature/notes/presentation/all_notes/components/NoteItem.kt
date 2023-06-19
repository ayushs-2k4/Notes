package com.ayushsinghal.notes.feature.notes.presentation.all_notes.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            )
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = { onClick() },
                onLongClick = { Log.d(TAG, "Long clicked on note with id: ${note.id}") }
            )
    )
    {
        Column() {

            if (note.tags.isNotEmpty()) {
                TagList(
                    modifier = Modifier
                        .padding(10.dp),
                    tags = note.tags
                )
            }

            if (note.title.isNotBlank()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (note.content.isNotBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Created: ${convertTimestampToDate(note.createdDate)}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 5.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun TagList(
    modifier: Modifier = Modifier,
    tags: List<String>
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        tags.forEach {
            MyTag(text = it)
        }
    }
}

@Composable
fun MyTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(vertical = 3.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .border(
                width = 1.dp,
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
//            .background(
//                color = MaterialTheme.colorScheme.onSurface
//            )
            .padding(horizontal = 2.dp, vertical = 2.dp)
    )
    {
        Text(
            text = text,
//            color = MaterialTheme.colorScheme.surface,
            fontSize = 15.sp
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun NoteItemPreview() {
    NoteItem(
        note = Note(
            title = "Title",
            content = "Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content Content ",
            tags = listOf(
                "Tag 1",
                "Tag 2",
                "Tag 2",
                "Tag 2",
                "Tag 2",
                "Tag 2",
                "Tag 2",
                "Tag 2",
                "Tag 2",
                "Tag 2"
            ),
            lastModifiedDate = 2,
            createdDate = 1
        ),
        onClick = {},
    )
}

@Preview
@Composable
fun MyTagPreview() {
    MyTag(
        text = "Tag 1"
    )
}

private fun convertTimestampToDate(timestamp: Long): String {
    val format = SimpleDateFormat("EEE, dd MMM h:mm a", Locale.getDefault())
    val date = Date(timestamp)
    return format.format(date)
}