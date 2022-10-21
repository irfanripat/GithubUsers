package com.astro.test.irfan.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.test.irfan.data.model.User
import com.astro.test.irfan.data.repository.UserRepository
import com.astro.test.irfan.data.repository.UserRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository = UserRepositoryImpl.getInstance()
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            viewModelState.value = viewModelState.value.copy(searchInput = query)
            searchUser(query)
        }
    }

    fun searchUser(query: String) {
        viewModelState.value = HomeViewModelState(isLoading = true)
        viewModelScope.launch {
            val response = repository.searchUsers(query)
            if (response.isSuccessful) {
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
}

sealed interface HomeUiState {
    val isLoading: Boolean
    val isNoResult: Boolean
    val searchInput: String

    data class HasUsers(
        val users: List<User>,
        override val isLoading: Boolean,
        override val isNoResult: Boolean,
        override val searchInput: String
    ) : HomeUiState

    data class NoUsers(
        override val isLoading: Boolean,
        override val isNoResult: Boolean,
        override val searchInput: String
    ) : HomeUiState
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val isNoResult: Boolean = false,
    val searchInput: String = "",
    val users: List<User> = emptyList()
) {
    fun toUiState(): HomeUiState {
        return if (users.isEmpty()) {
            HomeUiState.NoUsers(isLoading, isNoResult, searchInput)
        } else {
            HomeUiState.HasUsers(users, isLoading, isNoResult, searchInput)
        }
    }
}