package com.astro.test.irfan.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.test.lib_data.model.User
import com.astro.test.lib_data.repository.UserRepository
import com.astro.test.lib_data.repository.UserRepositoryImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository = UserRepositoryImpl.getInstance()
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = false))
    private var job: Job? = null

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun searchUser(query: String) {
        viewModelState.value = HomeViewModelState(isLoading = true)
        job?.run { if (isActive) cancel() }
        job = viewModelScope.launch {
            val response = repository.searchUsers(query)
            if (response.isSuccessful && !response.body()?.items.isNullOrEmpty()) {
                viewModelState.value = HomeViewModelState(
                    isLoading = false,
                    users = response.body()?.items ?: emptyList()
                )
            } else {
                viewModelState.value = HomeViewModelState(
                    isLoading = false,
                    isNoResult = true
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

sealed interface HomeUiState {
    val isLoading: Boolean
    val isNoResult: Boolean

    data class HasUsers(
        val users: List<User>,
        override val isLoading: Boolean,
        override val isNoResult: Boolean,
    ) : HomeUiState

    data class NoUsers(
        override val isLoading: Boolean,
        override val isNoResult: Boolean,
    ) : HomeUiState
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val isNoResult: Boolean = false,
    val users: List<User> = emptyList()
) {

    fun toUiState(): HomeUiState = if (users.isEmpty()) {
        HomeUiState.NoUsers(isLoading, isNoResult)
    } else {
        HomeUiState.HasUsers(users, isLoading, isNoResult)
    }
}