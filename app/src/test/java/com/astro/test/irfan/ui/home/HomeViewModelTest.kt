package com.astro.test.irfan.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.astro.test.lib_data.model.SearchUserResponse
import com.astro.test.lib_data.model.User
import com.astro.test.lib_data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val repository = mockk<UserRepository>(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `test when query is empty then the uiState should be HomeUiState_Empty`() = runTest {
        // when
        viewModel.searchUser("")
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState.value is HomeUiState.Empty)
    }

    @Test
    fun `test when api call return list of users then the uiState should be HomeUiState_Success`() = runTest {
        // given
        coEvery { repository.searchUsers("dummy") } returns Response.success(
            SearchUserResponse(dummyUsers)
        )

        // when
        viewModel.searchUser("dummy")
        advanceUntilIdle()

        // then
        assertEquals(HomeUiState.Success(dummyUsers), viewModel.uiState.value)
        assertEquals((viewModel.uiState.value as HomeUiState.Success).users[0].name, dummyUsers[0].name)
    }

    @Test
    fun `test when api call return empty list then the uiState should be HomeUiState_NoResult`() = runTest {
        // given
        coEvery { repository.searchUsers("dummy") } returns Response.success(
            SearchUserResponse(emptyList())
        )

        // when
        viewModel.searchUser("dummy")
        advanceUntilIdle()

        // then
        assertEquals(HomeUiState.NoResult, viewModel.uiState.value)
    }

    @Test
    fun `test when failed fetching data then the uiState should be HomeUiState_NoResult`() = runTest {
        // given
        coEvery { repository.searchUsers("dummy") } returns dummyErrorResponse

        // when
        viewModel.searchUser("dummy")
        advanceUntilIdle()

        // then
        assertEquals(HomeUiState.NoResult, viewModel.uiState.value)
    }

    companion object {
        val dummyUsers = listOf(
            User(
                id = 1,
                login = "dummy login",
                avatarUrl = "dummy avatar",
                name = "dummy name",
            )
        )

        val dummyErrorResponse : Response<SearchUserResponse> = Response.error(
            400,
            "".toResponseBody("application/json".toMediaTypeOrNull())
        )
    }
}