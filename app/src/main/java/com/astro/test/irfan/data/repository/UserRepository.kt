package com.astro.test.irfan.data.repository

import com.astro.test.irfan.data.model.SearchUserResponse
import retrofit2.Response

interface UserRepository {

    suspend fun searchUsers(query: String) : Response<SearchUserResponse>

}