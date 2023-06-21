package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ayushsinghal.notes.R
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.DeleteDialog
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TagInputDialog
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TransparentHintTextField
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen.ArchiveEvent
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen.ArchiveScreenViewModel
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen.TrashEvent
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen.TrashScreenViewModel
import com.ayushsinghal.notes.feature.notes.util.NoteStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    addEditNoteViewModel: AddEditNoteViewModel = hiltViewModel(),
    trashScreenViewModel: TrashScreenViewModel = hiltViewModel(),
    archiveScreenViewModel: ArchiveScreenViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val titleState = addEditNoteViewModel.noteTitle.value
    val contentState = addEditNoteViewModel.noteContent.value

    val createdDate by remember { mutableStateOf(addEditNoteViewModel.currentNoteCreatedDate) }
    val lastModifiedDate by remember { mutableStateOf(addEditNoteViewModel.currentNoteLastModifiedDate) }

    val myTagsList by addEditNoteViewModel.tagsLiveData.collectAsState(initial = emptyList())

    val showTagDialog = remember { mutableStateOf(false) }
    val tagIndex = remember { mutableStateOf(-1) }

    val showDeleteDialog = remember { mutableStateOf(false) }

    val currentNoteId = addEditNoteViewModel.currentNoteId

    val noteStatusArg = addEditNoteViewModel.noteStatus

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()


    BackHandler {
        addEditNoteViewModel.onEvent(AddEditNoteEvent.SaveNote)
        navController.popBackStack()
    }

    if (showTagDialog.value) {
        TagInputDialog(
            isNewTag = tagIndex.value == -1,  // To check if clicked on new tag or existing tag
            tagText = if (tagIndex.value == -1) {
                ""
            } else {
                myTagsList[tagIndex.value]
            },
            onClickAddOrUpdateTag = {
                if (tagIndex.value == -1) {
                    addEditNoteViewModel.onEvent(AddEditNoteEvent.OnPlusTagButtonClick(tag = it))
                } else {
                    addEditNoteViewModel.onEvent(
                        AddEditNoteEvent.OnChipClick(
                            type = "Update",
                            index = tagIndex.value,
                            tag = it
                        )
                    )
                }
                showTagDialog.value = false
                tagIndex.value = -1
            }, onClickCancelOrDelete = {
                addEditNoteViewModel.onEvent(
                    AddEditNoteEvent.OnChipClick(
                        type = "Delete",
                        index = tagIndex.value,
                        tag = "random"
                    )
                )
                showTagDialog.value = false
                tagIndex.value = -1
            })
    }

    if (showDeleteDialog.value) {
        if (noteStatusArg != NoteStatus.TrashedNote.type) {
            DeleteDialog(
                message = "The note will be moved to Trash",
                onCancelClick = {
                    showDeleteDialog.value = false
                },
                dismissButtonText = "Cancel",
                confirmButtonText = "Move to Trash",
                onDeleteClick = {
                    trashScreenViewModel.onTrashEvent(TrashEvent.MoveNoteToTrash(currentNoteId!!))
                    navController.popBackStack()
                    showDeleteDialog.value = false
                }
            )
        } else if (noteStatusArg == NoteStatus.TrashedNote.type) {
            DeleteDialog(
                message = "The note will be permanently deleted",
                dismissButtonText = "Cancel",
                confirmButtonText = "Delete Forever",
                onCancelClick = { showDeleteDialog.value = false },
                onDeleteClick = {
                    currentNoteId?.let {
                        TrashEvent.DeleteNoteForever(
                            it
                        )
                    }?.let {
                        trashScreenViewModel.onTrashEvent(it)
                    }
                    navController.popBackStack()
                    showDeleteDialog.value = false
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        /* TODO --> Complete Snack bar Functionality on touching text fields or chip area in Trash Note Status
        *   Add Color support to notes */
        topBar = {
            TopBar(
                noteStatusArg = noteStatusArg,
                navController = navController,
                archiveUnArchiveButtonClicked = {
                    if (noteStatusArg == NoteStatus.ExistingNote.type) {
                        archiveScreenViewModel.onArchiveEvent(
                            ArchiveEvent.MoveNoteToArchive(
                                currentNoteId!!
                            )
                        )
                        navController.navigateUp()
                    } else if (noteStatusArg == NoteStatus.ArchivedNote.type) {
                        archiveScreenViewModel.onArchiveEvent(
                            ArchiveEvent.MoveNoteFromArchive(
                                currentNoteId!!
                            )
                        )
                        navController.navigateUp()
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },

        bottomBar = {
            Log.d(TAG, "noteStatusArg: $noteStatusArg")
            BottomBar(
                noteStatusArg = noteStatusArg,
                onClickDeleteOrDeleteForever = {
                    showDeleteDialog.value = true
                },
                onClickShareOrRestore = {
                    if (noteStatusArg == NoteStatus.TrashedNote.type) {
                        currentNoteId?.let {
                            TrashEvent.RestoreNote(it)
                        }?.let {
                            trashScreenViewModel.onTrashEvent(it)
                            navController.navigateUp()
                        }
                    } else {
                        addEditNoteViewModel.viewModelScope.launch {
                            addEditNoteViewModel.onEvent(AddEditNoteEvent.ShareNote(context))
                        }
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

            TagsColumn(tagsList = myTagsList,
                onAddTagButtonClick = {
                    if (noteStatusArg == NoteStatus.TrashedNote.type) {
                        scope.launch {
                            snackbarHostState.showSnackbar(message = "Can't edit in Trash")
                        }
                    } else {
                        showTagDialog.value = true
                    }
                },
                onClick = {
                    if (noteStatusArg == NoteStatus.TrashedNote.type) {
                        scope.launch {
                            snackbarHostState.showSnackbar(message = "Can't edit in Trash")
                        }
                    } else {
                        showTagDialog.value = true
                        tagIndex.value = it
                    }
                }
            )

            TransparentHintTextField(
                givenText = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    addEditNoteViewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                singleLine = true,
                enabled = noteStatusArg != NoteStatus.TrashedNote.type,
                keyboardOptions = KeyboardOptions
                    (
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(),
                onFocusChange = { focusState ->
                    if (focusState.isFocused && noteStatusArg == NoteStatus.TrashedNote.type) {
                        scope.launch {
                            snackbarHostState.showSnackbar(message = "Can't edit in Trash")
                        }
                    } else {
                        addEditNoteViewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(focusState))
                    }
                },
                textStyle = MaterialTheme.typography.headlineLarge
            )

            TransparentHintTextField(
                givenText = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    addEditNoteViewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                enabled = noteStatusArg != NoteStatus.TrashedNote.type,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                keyboardActions = KeyboardActions(),
                onFocusChange = { focusState ->
                    if (focusState.isFocused && noteStatusArg == NoteStatus.TrashedNote.type) {
                        scope.launch {
                            snackbarHostState.showSnackbar(message = "Can't edit in Trash")
                        }
                    } else {
                        addEditNoteViewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(focusState))
                    }
                },
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

            Spacer(modifier = Modifier.height(5.dp))

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
    noteStatusArg: String,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    navController: NavController,
    archiveUnArchiveButtonClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
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
            IconButton(onClick = { archiveUnArchiveButtonClicked() }) {
                if (noteStatusArg == NoteStatus.ExistingNote.type) {
                    Icon(imageVector = Icons.Outlined.Archive, contentDescription = "Archive Note")
                } else if (noteStatusArg == NoteStatus.ArchivedNote.type) {
                    Log.d(TAG, "Yes, this note is Archived!!!")
                    Icon(
                        imageVector = Icons.Outlined.Unarchive,
                        contentDescription = "Archive Note"
                    )
                }
            }
        }
    )
}

@Composable
fun BottomBar(
    noteStatusArg: String,
    onClickDeleteOrDeleteForever: () -> Unit,
    onClickShareOrRestore: () -> Unit,
    onClickMenu: () -> Unit
) {

    BottomAppBar() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (
                noteStatusArg != NoteStatus.NewNote.type
            ) {
                if (noteStatusArg != NoteStatus.TrashedNote.type) {
                    IconButton(onClick = { onClickDeleteOrDeleteForever() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.delete_icon),
                            contentDescription = "Delete Note"
                        )
                    }
                } else if (noteStatusArg == NoteStatus.TrashedNote.type) {
                    IconButton(onClick = { onClickDeleteOrDeleteForever() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.delete_forever_icon),
                            contentDescription = "Delete Note Forever"
                        )
                    }
                }
            }

            if (noteStatusArg == NoteStatus.TrashedNote.type) {
                IconButton(onClick = { onClickShareOrRestore() }) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.restore_from_trash_icon),
                        contentDescription = "Restore Note"
                    )
                }
            } else {
                IconButton(onClick = { onClickShareOrRestore() }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share Note")
                }
            }

//            IconButton(onClick = { onClickMenu() }) {
//                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
//            }
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
                    Icon(imageVector = Icons.Outlined.AddBox, contentDescription = "Add Tag")
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