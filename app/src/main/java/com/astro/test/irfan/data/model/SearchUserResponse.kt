package com.astro.test.irfan.data.model

data class SearchUserResponse(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<User>
)