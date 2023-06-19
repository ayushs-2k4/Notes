package com.ayushsinghal.notes.feature.notes.presentation.all_notes

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ayushsinghal.notes.feature.authentication.presentation.signin.TAG
import com.ayushsinghal.notes.feature.notes.presentation.add_edit_note.components.MySearchBar
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.components.NoteItem
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.components.OrderSection
import com.ayushsinghal.notes.util.Screen
import kotlinx.coroutines.launch
import com.ayushsinghal.notes.feature.notes.presentation.all_notes.navigation_darwer.DrawerMenuItems
import com.ayushsinghal.notes.feature.notes.util.NoteStatus

@Composable
fun AllNotesScreen(
    navController: NavController,
    notesViewModel: NotesViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navigationDrawerItems = DrawerMenuItems.navigationDrawerItems

    val selectedItem = remember { mutableStateOf(navigationDrawerItems[0]) }
    val currentScreen = navController.currentDestination?.route

    val isGridLayoutSelected by remember { mutableStateOf(notesViewModel.isGridLayoutSelected) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.currentValue != DrawerValue.Closed,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                navigationDrawerItems.forEach { navigationDrawerItem ->
                    NavigationDrawerItem(
                        icon = {
//                            Icon(navigationDrawerItem.icon, contentDescription = null)
                            if (navigationDrawerItem.icon is ImageVector) {
                                Icon(
                                    imageVector = navigationDrawerItem.icon,
                                    contentDescription = null
                                )
                            } else if (navigationDrawerItem.icon is Int) {
                                Icon(
                                    painter = painterResource(navigationDrawerItem.icon),
                                    contentDescription = null
                                )
                            }
                        },

                        label = {
                            Text(
                                text = navigationDrawerItem.label,
                                fontFamily = FontFamily.SansSerif
                            )
                        },
                        selected = navigationDrawerItem.navigationDrawerScreen == currentScreen,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = navigationDrawerItem
                            navController.navigate(navigationDrawerItem.navigationDrawerScreen)
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .width(300.dp)
                            .height(50.dp)
                    )
                }
            }
        }
    ) {
        AllNotesScreenMainScreen(
            navController = navController,
            viewModel = notesViewModel,
            isGridLayoutSelected = isGridLayoutSelected.value,
            onClickChangeLayoutButton = {
                notesViewModel.changeLayoutType()
            },
            onMenuButtonPressed = {
                scope.launch {
                    drawerState.open()
                }
            }
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun AllNotesScreenMainScreen(
    navController: NavController,
    viewModel: NotesViewModel,
    isGridLayoutSelected: Boolean,
    onClickChangeLayoutButton: () -> Unit,
    onMenuButtonPressed: () -> Unit
) {
    val state = viewModel.state.value

    val searchResultsKey = remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopBar(
                isGridLayoutSelected = isGridLayoutSelected,
                onSortButtonPressed = { viewModel.onEvent(NotesEvent.ToggleOrderSection) },
                onMenuButtonPressed = { onMenuButtonPressed() },
                onQueryChange = {
                    viewModel.onEvent(NotesEvent.SearchNote(it))
                    searchResultsKey.value++
                },
                onClickChangeLayoutButton = { onClickChangeLayoutButton() },
                onSearch = {
                    viewModel.onEvent(NotesEvent.SearchNote(it))
                    searchResultsKey.value++
                }
            )
        },
        snackbarHost = { SnackbarHost(SnackbarHostState()) },
        floatingActionButton = { MyFloatingActionButton(navController = navController) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val offsetYOfOrderSection by animateDpAsState(
                targetValue = if (state.isOrderSectionVisible) 0.dp else (-30).dp,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            val offsetYOfAllNotes by animateDpAsState(
                targetValue = if (state.isOrderSectionVisible) 124.dp else 0.dp,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            // Order section
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .alpha(if (state.isOrderSectionVisible) 1f else 0f)
                    .offset(y = offsetYOfOrderSection),
                noteOrder = state.noteOrder,
                onOrderChange = {
                    viewModel.onEvent(NotesEvent.Order(it))
                }
            )

            // Notes grid
            AnimatedContent(targetState = isGridLayoutSelected, label = "", transitionSpec = {
//                slideInVertically { fullHeight -> fullHeight } + fadeIn() with slideOutVertically { fullHeight -> -fullHeight } + fadeOut()
                fadeIn() with fadeOut()
            }) { targetState ->
                if (targetState) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = offsetYOfAllNotes)
                    ) {
                        items(state.notes) { note ->
                            NoteItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clickable {
                                    },
                                note = note,
                                onClick = {
                                    Log.d(TAG, "id: ${note.id}")
                                    navController.navigate(
                                        Screen.AddEditNoteScreen.route +
                                                "?noteId=${note.id}&noteStatus=${NoteStatus.ExistingNote.type}"

                                    )
                                },
                            )
                        }
                    }
                } else if (!targetState) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = offsetYOfAllNotes)
                    ) {
                        items(state.notes) { note ->
                            NoteItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clickable {
                                    },
                                note = note,
                                onClick = {
                                    Log.d(TAG, "id: ${note.id}")
                                    navController.navigate(
                                        Screen.AddEditNoteScreen.route +
                                                "?noteId=${note.id}&noteStatus=${NoteStatus.ExistingNote.type}"

                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyFloatingActionButton(
    navController: NavController
) {
    FloatingActionButton(onClick = {
        navController.navigate("${Screen.AddEditNoteScreen.route}?noteStatus=${NoteStatus.NewNote.type}")

    }
    ) {
        Icon(imageVector = Icons.Default.Create, contentDescription = "Add Note")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isGridLayoutSelected: Boolean,
    onSortButtonPressed: () -> Unit,
    onMenuButtonPressed: () -> Unit,
    onQueryChange: (String) -> Unit,
    onClickChangeLayoutButton: () -> Unit,
    onSearch: (String) -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Your Notes")
        },
        actions = {

            MySearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                isGridLayoutSelected = isGridLayoutSelected,
                onSortButtonPressed = { onSortButtonPressed() },
                onMenuButtonPressed = { onMenuButtonPressed() },
                onQueryChange = { onQueryChange(it) },
                onClickChangeLayoutButton = { onClickChangeLayoutButton() },
                onSearch = { onSearch(it) }
            )
        }
    )
}

//@Preview(showSystemUi = true)
//@Composable
//fun AllNotesScreenPreview() {
//
//    val noteDatabase = Room
//        .databaseBuilder(
//            LocalContext.current,
//            NoteDatabase::class.java,
//            NoteDatabase.DATABASE_NAME
//        )
//        .build()
//
//    val noteRepositoryImpl = NoteRepositoryImpl(noteDatabase.noteDao)
//
//    val notesUseCases = NoteUseCases(
//        addNoteUseCase = AddNoteUseCase(noteRepositoryImpl),
//        deleteNoteUseCase = DeleteNoteUseCase(noteRepositoryImpl),
//        getNotesUseCase = GetNotesUseCase(noteRepositoryImpl),
//        searchNotesUseCase = SearchNotesUseCase(noteRepositoryImpl)
//    )
//
//    AllNotesScreen(
//        navController = NavController(LocalContext.current),
//        viewModel = NotesViewModel(notesUseCases)
//    )
//}

//@Preview
//@Composable
//fun MyFloatingActionButtonPreview() {
//    MyFloatingActionButton()
//}