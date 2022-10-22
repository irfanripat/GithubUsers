package com.astro.test.irfan.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.test.lib_data.model.User
import com.astro.test.lib_data.repository.UserRepository
import com.astro.test.lib_data.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private typealias State = HomeViewModelState

class HomeViewModel(
    private val repository: UserRepository = UserRepositoryImpl.getInstance()
) : ViewModel() {

    private val viewModelState = MutableStateFlow(State())
    private var job: Job? = null

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun searchUser(query: String) {
        updateStateValue(State(isLoading = true, searchInput = query))
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            if (query.isEmpty()) {
                updateStateValue(State(isLoading = false, searchInput = query))
            } else {
                val response = repository.searchUsers(query)
                val users = response.body()?.items ?: emptyList()

                if (response.isSuccessful && users.isNotEmpty()) {
                    updateStateValue(State(isLoading = false, searchInput = query, users = users))
                } else {
                    updateStateValue(
                        State(
                            isLoading = false,
                            searchInput = query,
                            isNoResult = true
                        )
                    )
                }
            }
        }
    }

    private fun updateStateValue(state: State) {
        viewModelState.value = state
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val isNoResult: Boolean = false,
    val searchInput: String = "",
    val users: List<User> = emptyList()
) {

    fun toUiState(): HomeUiState = when {
        searchInput.isEmpty() -> HomeUiState.Empty
        isLoading -> HomeUiState.Loading
        !isLoading && isNoResult -> HomeUiState.NoResult
        else -> HomeUiState.Success(users = users)
    }
}