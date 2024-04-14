package org.haidy.storenotes.ui.screens.notes

import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@OptIn(ExperimentalStoreApi::class)
@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository) :
    BaseViewModel<NotesUiState, NotesUiEffect>(NotesUiState()), NotesInteractionListener {

    private var notesStore: MutableStore<NotesKey, Any>? = null

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
        notesStore = store
        val getNotesRequest =
            StoreReadRequest.fresh(NotesKey.Read.ReadAllNotes)

        store.stream<StoreReadResponse<List<Note>>>(getNotesRequest).collect { response ->
            when (response) {
                is StoreReadResponse.Data -> {
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
        _state.update { it.copy(isLoading = false) }
        sendNewEffect(NotesUiEffect.ShowErrorMessage(error.message ?: "Request failed"))
    }

    override fun onClickNote(noteId: String) {
        sendNewEffect(NotesUiEffect.NavigateToNoteDetails(noteId))
    }

    override fun onClickAddNote() {
        if (notesStore != null) {
            val note = Note(id = "", content = _state.value.newNoteContent)
            val addNoteRequest =
                StoreWriteRequest.of<NotesKey, Any, Any>(NotesKey.Write.Create, note)
            tryRequest(
                block = { notesStore!!.write(addNoteRequest) },
                onSuccess = {
                    _state.update { it.copy(newNoteContent = "") }
                },
                onError = ::onError
            )
        }
    }

    override fun onClickDeleteAllNotes() {
        if (notesStore != null) {
            val deleteAllNotes =
                StoreWriteRequest.of<NotesKey, Any, Any>(NotesKey.Clear.ClearAllNotes, Note("", ""))
            tryRequest(
                block = { notesStore!!.write(deleteAllNotes) },
                onSuccess = {},
                onError = ::onError
            )
        }
    }

    override fun onNewNoteContentChanged(content: String) {
        _state.update { it.copy(newNoteContent = content) }
    }

}