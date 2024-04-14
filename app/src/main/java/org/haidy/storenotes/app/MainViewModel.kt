package org.haidy.storenotes.app

import dagger.hilt.android.lifecycle.HiltViewModel
import org.haidy.storenotes.repository.UserRepository
import org.haidy.storenotes.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) :
    BaseViewModel<MainUiState, Nothing>(MainUiState()) {
    init {
        getIfUserIsLoggedIn()
    }

    private fun getIfUserIsLoggedIn() {
        tryRequest(
            block = { userRepository.getUserId() },
            onSuccess = { userId ->
                _state.value = MainUiState(isLoggedIn = userId.isNotEmpty())
            },
            onError = {}
        )
    }
}

data class MainUiState(
    val isLoggedIn: Boolean = false
)