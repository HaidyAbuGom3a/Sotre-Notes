package org.haidy.storenotes.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<State, Effect>(state: State) : ViewModel() {

    protected val _state = MutableStateFlow(state)
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()


    protected fun sendNewEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    protected fun <Response> tryStoreRequest(
        block: suspend () -> Response,
        onSuccess: suspend (Response) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = block()
                    onSuccess(response)
                }catch (e: Exception){
                    onError(e)
                }
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    protected fun <Response> tryRequest(
        block: suspend () -> Response,
        onSuccess: (Response) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = block()
                    onSuccess(response)
                }catch (e: Exception){
                    onError(e)
                }
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

}