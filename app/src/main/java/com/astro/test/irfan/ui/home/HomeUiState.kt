package com.astro.test.irfan.ui.home

import com.astro.test.lib_data.model.User

sealed interface HomeUiState {

    data class Success(val users: List<User>) : HomeUiState

    object Loading : HomeUiState

    object NoResult : HomeUiState

    object Empty : HomeUiState
}