package com.astro.test.irfan.data.repository

import com.astro.test.irfan.data.model.SearchUserResponse
import com.astro.test.irfan.data.model.User
import retrofit2.Response

interface UserRepository {

    suspend fun searchUsers(query: String) : Response<SearchUserResponse>

    suspend fun getUser(username: String) : Response<User>

}