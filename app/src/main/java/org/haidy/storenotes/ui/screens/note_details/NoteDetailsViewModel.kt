package org.haidy.storenotes.ui.screens.note_details

import androidx.lifecycle.SavedStateHandle
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

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel<NoteDetailsUiState, NoteDetailsUiEffect>(NoteDetailsUiState()),
    NoteDetailsInteractionListener {

    private var notesStore: MutableStore<NotesKey, Any>? = null
    private val args = NoteDetailsArgs(savedStateHandle)

    init {
        getNoteDetails()
    }

    private fun getNoteDetails() {
        tryStoreRequest(
            block = { notesRepository.getNoteStore() },
            onSuccess = ::onGetStoreSuccess,
            onError = ::onError
        )
    }

    @OptIn(ExperimentalStoreApi::class)
    private suspend fun onGetStoreSuccess(store: MutableStore<NotesKey, Any>) {
        notesStore = store
        val getNotesRequest =
            StoreReadRequest.cached(NotesKey.Read.ReadByNoteId(args.noteId), true)

        store.stream<StoreReadResponse<Note>>(getNotesRequest).collect { response ->
            when (response) {
                is StoreReadResponse.Data -> {
                    _state.update {
                        it.copy(
                            noteId = (response.value as Note).id,
                            noteContent = (response.value as Note).content,
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
        sendNewEffect(NoteDetailsUiEffect.ShowErrorMessage(error.message ?: "Request failed"))
    }

    override fun onContentChanged(content: String) {
        _state.update { it.copy(noteContent = content) }
    }

    @OptIn(ExperimentalStoreApi::class)
    override fun onClickDeleteNote() {
        if (notesStore != null) {
            val deleteNote =
                StoreWriteRequest.of<NotesKey, Any, Any>(
                    NotesKey.Clear.ById(args.noteId),
                    _state.value.toNote()
                )
            tryRequest(
                block = { notesStore!!.write(deleteNote) },
                onSuccess = { sendNewEffect(NoteDetailsUiEffect.NavigateBack) },
                onError = ::onError
            )
        }
    }

    @OptIn(ExperimentalStoreApi::class)
    override fun onClickUpdateNote() {
        if (notesStore != null) {
            val updateNote =
                StoreWriteRequest.of<NotesKey, Any, Any>(
                    NotesKey.Write.Update,
                    _state.value.toNote()
                )
            tryRequest(
                block = { notesStore!!.write(updateNote) },
                onSuccess = { sendNewEffect(NoteDetailsUiEffect.NavigateBack) },
                onError = ::onError
            )
        }
    }
}