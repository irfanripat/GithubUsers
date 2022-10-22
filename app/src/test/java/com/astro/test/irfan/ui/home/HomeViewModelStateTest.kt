package com.astro.test.irfan.ui.home

import com.astro.test.lib_data.model.User
import org.junit.Assert.*
import org.junit.Test

class HomeViewModelStateTest {

    @Test
    fun `test when search input is empty then it should be HomeUiState_Empty`() {
        val dummyState = HomeViewModelState(searchInput = "")
        assertEquals(dummyState.toUiState(), HomeUiState.Empty)
    }

    @Test
    fun `test when it's loading then it should be HomeUiState_Loading`() {
        val dummyState = HomeViewModelState(isLoading = true, searchInput = "dummy")
        assertEquals(dummyState.toUiState(), HomeUiState.Loading)
    }

    @Test
    fun `test when it's error or return empty list then it should be HomeUiState_NoResult`() {
        val dummyState =
            HomeViewModelState(searchInput = "dummy", isLoading = false, isNoResult = true)
        assertEquals(dummyState.toUiState(), HomeUiState.NoResult)
    }

    @Test
    fun `test when it's success get users then it should be HomeUiState_Success`() {
        val user = User(
            id = 1,
            login = "dummy login",
            avatarUrl = "dummy avatar",
            name = "dummy name",
        )
        val dummyState = HomeViewModelState(
            searchInput = "dummy",
            isLoading = false,
            users = listOf(user)
        )
        assertEquals(dummyState.toUiState(), HomeUiState.Success(users = listOf(user)))
    }
}