package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TagInputDialog
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val createdDate by remember { mutableStateOf(viewModel.currentNotesCreatedDate2) }
    val lastModifiedDate by remember { mutableStateOf(viewModel.currentNotesLastModifiedDate2) }

    val myTagsList by viewModel.tagsLiveData.collectAsState(initial = emptyList())

    val showDialog = remember { mutableStateOf(false) }
    val tagIndex = remember { mutableStateOf(-1) }

    BackHandler {
        viewModel.onEvent(AddEditNoteEvent.SaveNote)
        navController.popBackStack()
    }

    if (showDialog.value) {
        TagInputDialog(
            isNewTag = tagIndex.value == -1,  // To check if clicked on new tag or existing tag
            tagText = if (tagIndex.value == -1) {
                ""
            } else {
                myTagsList[tagIndex.value]
            },
            onClickAddOrUpdateTag = {
                if (tagIndex.value == -1) {
                    viewModel.onEvent(AddEditNoteEvent.OnPlusTagButtonClick(tag = it))
                } else {
                    viewModel.onEvent(
                        AddEditNoteEvent.OnChipClick(
                            type = "Update",
                            index = tagIndex.value,
                            tag = it
                        )
                    )
                }
                showDialog.value = false
                tagIndex.value = -1
            }, onClickCancelOrDelete = {
                viewModel.onEvent(
                    AddEditNoteEvent.OnChipClick(
                        type = "Delete",
                        index = tagIndex.value,
                        tag = "random"
                    )
                )
                showDialog.value = false
                tagIndex.value = -1
            })
    }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController
            )
        },

        bottomBar = {
            BottomBar(
                onClickDelete = {
                    viewModel.viewModelScope.launch {
                        viewModel.onEvent(
                            AddEditNoteEvent.DeleteNote(
                                context = context,
                                navController = navController
                            )
                        )
                        navController.popBackStack()
                    }
                },
                onClickShare = {
                    viewModel.viewModelScope.launch {
                        viewModel.onEvent(AddEditNoteEvent.ShareNote(context))
                    }
                },
                onClickMenu = {}
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState()),

            ) {

            TagsColumn(tagsList = myTagsList, onAddTagButtonClick = {
                showDialog.value = true
            },
                onClick = {
                    tagIndex.value = it
                    showDialog.value = true
                }
            )

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                textStyle = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Created: ${convertTimestampToDate(createdDate.value)}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Updated: ${convertTimestampToDate(lastModifiedDate.value)}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
            }
        },
        actions = {
        }
    )
}

@Composable
fun BottomBar(
    onClickDelete: () -> Unit,
    onClickShare: () -> Unit,
    onClickMenu: () -> Unit
) {

    BottomAppBar() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onClickDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
            }

            IconButton(onClick = { onClickShare() }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share Note")
            }

            IconButton(onClick = { onClickMenu() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TagsColumn(
    tagsList: List<String>,
    onAddTagButtonClick: () -> Unit,
    onClick: (index: Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    )
    {
        LazyRow()
        {
            items(tagsList.size)
            { index ->
                AssistChip(
                    onClick = { onClick(index) },
                    label = { Text(text = tagsList[index]) },
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                )
            }

            item {
                IconButton(onClick = { onAddTagButtonClick() }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Tag")
                }
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun AddEditNoteScreenPreview() {
//
//    val noteDatabase = Room
//        .databaseBuilder(
//            LocalContext.current,
//            NoteDatabase::class.java,
//            NoteDatabase.DATABASE_NAME
//        )
//        .build()
//
//    val noteRepository = NoteRepositoryImpl(noteDatabase.noteDao)
//
//
//    val noteUseCases = NoteUseCases(
//        addNoteUseCase = AddNoteUseCase(noteRepository),
//        deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
//        getNotesUseCase = GetNotesUseCase(noteRepository)
//    )
//
//    val addEditNoteUseCases = AddEditNoteUseCases(
//        deleteNoteAddEditUseCase = DeleteNoteAddEditUseCase(noteRepository),
//        getNoteUseCase = GetNoteUseCase(noteRepository),
//        shareNoteUseCase = ShareNoteAddEditUseCase()
//    )
//
//    val addEditNoteViewModel = AddEditNoteViewModel(
//        noteUseCases = noteUseCases,
//        addEditNoteUseCases = addEditNoteUseCases,
//        savedStateHandle = SavedStateHandle(),
//    )
//
//    AddEditNoteScreen(
//        navController = NavController(LocalContext.current),
//        viewModel = addEditNoteViewModel
//    )
//}

//@Preview
//@Composable
//fun TopBarPreview() {
//
//    val noteDatabase = Room
//        .databaseBuilder(
//            LocalContext.current,
//            NoteDatabase::class.java,
//            NoteDatabase.DATABASE_NAME
//        )
//        .build()
//
//    val noteRepository = NoteRepositoryImpl(noteDatabase.noteDao)
//
//
//    val noteUseCases = NoteUseCases(
//        addNoteUseCase = AddNoteUseCase(noteRepository),
//        deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
//        getNotesUseCase = GetNotesUseCase(noteRepository)
//    )
//
//    val addEditNoteUseCases = AddEditNoteUseCases(
//         deleteNoteAddEditUseCase = DeleteNoteAddEditUseCase(noteRepository),
//        getNoteUseCase = GetNoteUseCase(noteRepository)
//    )
//
//    val addEditNoteViewModel = AddEditNoteViewModel(
//        noteUseCases = noteUseCases,
//        addEditNoteUseCases = addEditNoteUseCases,
//        savedStateHandle =   SavedStateHandle()
//    )
//
//    TopBar(
//        viewModel = addEditNoteViewModel,
//        navController = NavController(LocalContext.current)
//    )
//}

//@Preview
//@Composable
//fun BottomBarPreview() {
//    BottomBar(
//        onClickDelete = {},
//        onClickShare = {},
//        onClickMenu = {}
//    )
//}

//@Preview(showSystemUi = true)
//@Composable
//fun TagsColumnPreview() {
//
//    TagsColumn(tagsList = tagsList, onAddTagButtonClick = {}, onClick = {})
//}

private fun convertTimestampToDate(timestamp: Long): String {
//    val format = SimpleDateFormat("yyyy-MM-dd h:mm:ss a", Locale.getDefault())
    val format = SimpleDateFormat("EEE, dd MMM h:mm a", Locale.getDefault())
    val date = Date(timestamp)
    return format.format(date)
}