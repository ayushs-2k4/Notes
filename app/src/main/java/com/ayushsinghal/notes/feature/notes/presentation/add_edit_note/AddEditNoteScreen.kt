package com.ayushsinghal.notes.feature.notes.presentation.add_edit_note

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ayushsinghal.notes.R
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.domain.model.Note
import com.ayushsinghal.notes.feature.notes.domain.model.Note.Companion.getColors
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.DeleteDialog
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TagInputDialog
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.TransparentHintTextField
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen.ArchiveEvent
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.archive_screen.ArchiveScreenViewModel
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen.TrashEvent
import com.ayushsinghal.notes.feature.notes.presentation.navigation_drawer_screens.trash_screen.TrashScreenViewModel
import com.ayushsinghal.notes.feature.notes.util.NoteStatus
import com.ayushsinghal.notes.util.Screen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    val noteColorIndex = addEditNoteViewModel.noteColorIndex
    val noteColor = getColors()[noteColorIndex.value]

    val noteBackgroundImageIndex = addEditNoteViewModel.noteBackgroundImageIndex
    val noteBackgroundImage = Note.getBackgroundImages()[noteBackgroundImageIndex.value]
    val backgroundColor = if ((noteBackgroundImageIndex.value == 0)) {
        noteColor
    } else {
        Color.Transparent
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }


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
                confirmButtonTextColor = Color.Red,
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



    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            containerColor = noteColor,
            windowInsets = WindowInsets(
                0,
                0,
                0,
                0
            ) // To make the status bar color dim when ModalBottomSheet is shown
        ) {
            Text(
                text = "Color",
                modifier = Modifier
                    .padding(start = 10.dp)
            )

            ColorChooser(
                colorValues = getColors().map {
                    it.toArgb()
                },
                selectedColorIndex = noteColorIndex.value,
                onClickColor = {
                    addEditNoteViewModel.onEvent(AddEditNoteEvent.ChangeColor(it))
                })

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Background",
                modifier = Modifier
                    .padding(start = 10.dp)
            )

            BackgroundChooser(
                imageValues = Note.getBackgroundImages(),
                selectedImageIndex = noteBackgroundImageIndex.value,
                onClickBackground = {
                    addEditNoteViewModel.onEvent(AddEditNoteEvent.ChangeBackground(it))
                }
            )

            Spacer(modifier = Modifier.height(35.dp))
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    )
    {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(noteBackgroundImage),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop
        )

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopBar(
                    noteStatusArg = noteStatusArg,
                    color = backgroundColor,
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
                            navController.navigate(
                                Screen.AddEditNoteScreen.route +
                                        "?noteId=${currentNoteId}&noteStatus=${NoteStatus.ExistingNote.type}"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },

            bottomBar = {
                BottomBar(
                    noteStatusArg = noteStatusArg,
                    color = backgroundColor,
                    onClickPaletteIcon = {
                        if (noteStatusArg != NoteStatus.TrashedNote.type) {
                            openBottomSheet = true
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Can't edit in Trash")
                            }
                        }
                    },
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
                                navController.navigate(
                                    Screen.AddEditNoteScreen.route +
                                            "?noteId=${currentNoteId}&noteStatus=${NoteStatus.ExistingNote.type}"
                                )
                            }
                        } else {
                            addEditNoteViewModel.viewModelScope.launch {
                                addEditNoteViewModel.onEvent(AddEditNoteEvent.ShareNote(context))
                            }
                        }
                    },
                    onClickMakeACopy = {
                        addEditNoteViewModel.onEvent(AddEditNoteEvent.MakeACopy)
                        navController.popBackStack()
                    },
                    onClickMenu = {}
                )
            },

//            containerColor = noteColor,
            containerColor = backgroundColor,
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState())

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
                            addEditNoteViewModel.onEvent(
                                AddEditNoteEvent.ChangeTitleFocus(
                                    focusState
                                )
                            )
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
                            addEditNoteViewModel.onEvent(
                                AddEditNoteEvent.ChangeContentFocus(
                                    focusState
                                )
                            )
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

                if ((noteBackgroundImageIndex.value != 0) && (noteColorIndex.value != 0)) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .background(noteColor)
                            .combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(),
                                onClick = {
                                    if (noteStatusArg != NoteStatus.TrashedNote.type) {
                                        openBottomSheet = true
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(message = "Can't edit in Trash")
                                        }
                                    }
                                },
                            )
                            .border(
                                width = 2.dp,
                                color = Color.Black,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    noteStatusArg: String,
    color: Color,
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = color
        ),
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
    color: Color,
    onClickPaletteIcon: () -> Unit,
    onClickDeleteOrDeleteForever: () -> Unit,
    onClickMakeACopy: () -> Unit,
    onClickShareOrRestore: () -> Unit,
    onClickMenu: () -> Unit
) {

    BottomAppBar(
        containerColor = color
    ) {

        IconButton(onClick = { onClickPaletteIcon() }) {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = "New Icon"
            )
        }

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

            if (noteStatusArg == NoteStatus.ExistingNote.type) {
                IconButton(onClick = { onClickMakeACopy() }) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Note")
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

@Composable
fun ColorChooser(
    colorValues: List<Int>,
    selectedColorIndex: Int,
    onClickColor: (colorIndex: Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(colorValues.size) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
                    .background(Color(colorValues[it]))
                    .clickable {
                        onClickColor(it)
                    }
                    .border(
                        width = if (selectedColorIndex == it) 2.dp else 1.dp,
                        color = if (selectedColorIndex == it) MaterialTheme.colorScheme.primary else Color.Black,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun BackgroundChooser(
    imageValues: List<Int>,
    selectedImageIndex: Int,
    onClickBackground: (backgroundIndex: Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(imageValues.size) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
                    .paint(painterResource(imageValues[it]), contentScale = ContentScale.Crop)
                    .clickable {
                        onClickBackground(it)
                    }
                    .border(
                        width = if (selectedImageIndex == it) 2.dp else 1.dp,
                        color = if (selectedImageIndex == it) MaterialTheme.colorScheme.primary else Color.Black,
                        shape = CircleShape
                    )
            )
        }
    }
}

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

//@Preview
//@Composable
//fun BottomSheetContentPreview() {
//    val list = listOf(
//        RedOrange.toArgb(), RedPink.toArgb(), BabyBlue.toArgb(), Violet.toArgb(),
//        LightGreen.toArgb()
//    )
//    var selectedColor by remember {
//        mutableStateOf(BabyBlue)
//    }
//    BottomSheetContent(
//        colorValues = list,
////        selectedColor = MaterialTheme.colorScheme.surface,
//        selectedColor = selectedColor,
//        onClickColor = {
//            selectedColor = Color(it)
//        }
//    )
//}

//@Preview(showSystemUi = true)
//@Composable
//fun BackgroundChooserPreview() {
//    BackgroundChooser(
//        imageValues = Note.getBackgroundImages(),
//        selectedImageIndex = 1,
//        onClickBackground = {}
//    )
//}

private fun convertTimestampToDate(timestamp: Long): String {
//    val format = SimpleDateFormat("yyyy-MM-dd h:mm:ss a", Locale.getDefault())
    val format = SimpleDateFormat("EEE, dd MMM h:mm a", Locale.getDefault())
    val date = Date(timestamp)
    return format.format(date)
}