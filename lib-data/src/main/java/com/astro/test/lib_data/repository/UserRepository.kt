package com.astro.test.lib_data.repository

import com.astro.test.lib_data.model.SearchUserResponse
import retrofit2.Response

interface UserRepository {

    suspend fun searchUsers(query: String) : Response<SearchUserResponse>

}