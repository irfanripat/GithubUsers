package com.astro.test.irfan.ui.home

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.astro.test.irfan.ui.theme.GithubUsersTheme
import com.astro.test.irfan.waitUntilDoesNotExist
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            GithubUsersTheme {
                HomeScreen()
            }
        }
    }

    @Test
    fun test_on_empty_state() {
        composeTestRule.run {
            onNodeWithText("No data found").assertDoesNotExist()
            onNodeWithText("Loading...").assertDoesNotExist()
        }
    }

    @Test
    fun test_on_loading_state() {
        composeTestRule.run {
            onNodeWithText("Search").performTextInput("Irfan Ripat")
            onNodeWithText("Loading...").assertIsDisplayed()
        }
    }

    @Test
    fun test_on_no_users_found() {
        composeTestRule.run {
            onNodeWithText("Search").performTextInput("Irfan Ripatttt")
            waitUntilDoesNotExist(hasTextExactly("Loading..."), 10000)
            onNodeWithText("No users found").assertIsDisplayed()
            onAllNodesWithContentDescription("avatar").assertCountEquals(0)
        }
    }

    @Test
    fun test_on_success_search_user() {
        composeTestRule.run {
            onNodeWithText("Search").performTextInput("Irfan")
            onNodeWithText("Loading...").assertIsDisplayed()
            waitUntilDoesNotExist(hasTextExactly("Loading..."), 10000)
            onAllNodesWithContentDescription("avatar").assertCountEquals(5)
            onNodeWithText("No users found").assertDoesNotExist()
        }
    }
}
