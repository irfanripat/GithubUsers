package com.astro.test.irfan.data.repository

import com.astro.test.irfan.data.model.SearchUserResponse
import com.astro.test.irfan.data.model.User
import com.astro.test.irfan.data.network.RetrofitHelper
import retrofit2.Response

class UserRepositoryImpl : UserRepository {

    override suspend fun searchUsers(query: String): Response<SearchUserResponse> {
        return RetrofitHelper.apiService.searchUsers(query)
    }

    override suspend fun getUser(username: String): Response<User> {
        return RetrofitHelper.apiService.getUser(username)
    }
}