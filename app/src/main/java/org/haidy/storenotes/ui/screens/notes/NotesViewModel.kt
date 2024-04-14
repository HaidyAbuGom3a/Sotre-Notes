package org.haidy.storenotes.ui.screens.notes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import org.haidy.storenotes.repository.NotesRepository
import org.haidy.storenotes.repository.model.Note
import org.haidy.storenotes.repository.model.NotesKey
import org.haidy.storenotes.ui.base.BaseViewModel
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.MutableStore
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreWriteRequest
import org.mobilenativefoundation.store.store5.impl.extensions.get
import javax.inject.Inject

@OptIn(ExperimentalStoreApi::class)
@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository) :
    BaseViewModel<NotesUiState, NotesUiEffect>(NotesUiState()), NotesInteractionListener {

    init {
        getNotes()
    }

    @OptIn(ExperimentalStoreApi::class)
    private fun getNotes() {
        tryStoreRequest(
            block = { notesRepository.getNoteStore() },
            onSuccess = ::onGetNotesStoreSuccess,
            onError = ::onError
        )
    }

    @OptIn(ExperimentalStoreApi::class)
    private suspend fun onGetNotesStoreSuccess(store: MutableStore<NotesKey, Any>) {
        _state.update { it.copy(store = store) }
        val getNotesRequest =
            StoreReadRequest.fresh(NotesKey.Read.ReadAllNotes)

        store.stream<StoreReadResponse<List<Note>>>(getNotesRequest).collect { response ->
            when (response) {
                is StoreReadResponse.Data -> {
                    Log.e("HAIDDYY", "data: ${response.value}")
                    _state.update {
                        it.copy(
                            notes = response.value as List<Note>,
                            isLoading = false
                        )
                    }
                }

                is StoreReadResponse.Error.Exception -> {
                    onError(Exception(response.error))
                }

                is StoreReadResponse.Error.Message -> {
                    onError(Exception(response.message))
                }

                is StoreReadResponse.Loading -> _state.update { it.copy(isLoading = true) }
                else -> _state.update { it.copy(isLoading = false) }
            }

        }
    }

    private fun onError(error: Exception) {
        Log.e("HAIDDYY", "onError: ${error.message}")
        _state.update { it.copy(isLoading = false) }
        sendNewEffect(NotesUiEffect.ShowErrorMessage(error.message ?: "Request failed"))
    }

    override fun onClickNote(noteId: String) {
        sendNewEffect(NotesUiEffect.NavigateToNoteDetails(noteId))
    }

    override fun onClickAddNote() {
        val store = _state.value.store
        if (store != null) {
            val note = Note(id = "", content = _state.value.newNoteContent)
            val addNoteRequest =
                StoreWriteRequest.of<NotesKey, Any, Any>(NotesKey.Write.Create, note)
            tryRequest(
                block = { store.write(addNoteRequest) },
                onSuccess = {
                    Log.e("HAIDDYY", "success add note")
                    _state.update { it.copy(newNoteContent = "") }
                },
                onError = ::onError
            )
        }
    }

    override fun onClickDeleteAllNotes() {
        val store = _state.value.store
        if (store != null) {
            val deleteAllNotes =
                StoreWriteRequest.of<NotesKey, Any, Any>(NotesKey.Clear.ClearAllNotes, Note("", ""))
            tryRequest(
                block = { store.write(deleteAllNotes) },
                onSuccess = {},
                onError = ::onError
            )
        }
    }

    override fun onNewNoteContentChanged(content: String) {
        _state.update { it.copy(newNoteContent = content) }
    }

}