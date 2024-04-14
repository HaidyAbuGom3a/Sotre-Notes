package org.haidy.storenotes.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.haidy.storenotes.R
import org.haidy.storenotes.ui.composable.StoreNotesButton
import org.haidy.storenotes.ui.composable.StoreNotesTextField
import org.haidy.storenotes.ui.screens.note_details.navigateToNoteDetails
import org.haidy.storenotes.ui.theme.Pink80
import org.haidy.storenotes.ui.theme.Typography
import org.haidy.storenotes.ui.theme.White
import org.haidy.storenotes.ui.utils.EffectHandler

@Composable
fun NotesScreen() {
    val viewModel: NotesViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    EffectHandler(effects = viewModel.effect) { effect, navController ->
        when (effect) {
            is NotesUiEffect.NavigateToNoteDetails -> navController.navigateToNoteDetails(effect.noteId)
            is NotesUiEffect.ShowErrorMessage -> {
                scope.launch {
                    snackBarState.showSnackbar(effect.message)
                }
            }
        }

    }
    NotesScreenContent(state, snackBarState, viewModel)
}

@Composable
fun NotesScreenContent(
    state: NotesUiState,
    snackBarState: SnackbarHostState,
    listener: NotesInteractionListener
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { listener.onClickDeleteAllNotes() }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarState) {
                Snackbar(
                    snackbarData = it,
                    contentColor = White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .heightIn(min = 48.dp, max = 160.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StoreNotesTextField(
                    onValueChange = { listener.onNewNoteContentChanged(it) },
                    text = state.newNoteContent,
                    hint = "Write your note here",
                    modifier = Modifier
                        .width(200.dp)
                        .padding(end = 8.dp)
                )
                StoreNotesButton(
                    onClick = { listener.onClickAddNote() },
                    text = "Add note",
                    enabled = state.newNoteContent.isNotEmpty(),
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(end = 4.dp)
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(state.notes) { index, note ->
                    NoteItem(
                        onClick = { id -> listener.onClickNote(id) },
                        title = "Note ${index + 1}",
                        noteId = note.id,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteItem(
    onClick: (String) -> Unit,
    title: String,
    noteId: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .height(100.dp)
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(Pink80)
            .clickable { onClick(noteId) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, style = Typography.displaySmall)
    }
}