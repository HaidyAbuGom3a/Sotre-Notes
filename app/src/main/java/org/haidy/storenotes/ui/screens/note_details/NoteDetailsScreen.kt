package org.haidy.storenotes.ui.screens.note_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.haidy.storenotes.ui.composable.StoreNotesButton
import org.haidy.storenotes.ui.theme.Typography
import org.haidy.storenotes.ui.theme.White

@Composable
fun NoteDetailsScreen() {
    val viewModel: NoteDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    NoteDetailsContent(state, viewModel)
}

@Composable
fun NoteDetailsContent(state: NoteDetailsUiState, listener: NoteDetailsInteractionListener) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        BasicTextField(
            value = state.noteContent,
            onValueChange = { listener.onContentChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            textStyle = Typography.bodyLarge
        )
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            StoreNotesButton(
                onClick = { listener.onClickUpdateNote() },
                text = "Update note",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
            )
            StoreNotesButton(
                onClick = { listener.onClickDeleteNote() },
                text = "Delete note",
                modifier = Modifier.weight(1f)
            )
        }
    }
}


