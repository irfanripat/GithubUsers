package com.astro.test.irfan

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 1_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}

fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
}