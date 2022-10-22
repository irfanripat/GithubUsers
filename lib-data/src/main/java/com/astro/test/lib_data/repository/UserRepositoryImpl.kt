package com.astro.test.lib_data.repository

import com.astro.test.lib_data.model.SearchUserResponse
import com.astro.test.lib_data.model.User
import com.astro.test.lib_data.network.RetrofitHelper
import retrofit2.Response

class UserRepositoryImpl : UserRepository {

    override suspend fun searchUsers(query: String): Response<SearchUserResponse> {
        val users = RetrofitHelper.apiService.searchUsers(query)
        return if (users.isSuccessful) {
            val usersWithName = users.body()?.items?.map {
                val user = getUser(it.login)
                if (user.isSuccessful) {
                    user.body()
                } else {
                    it
                }
            }
            usersWithName?.let {
                Response.success(SearchUserResponse(it.map { user -> user as User }))
            } ?: Response.success(SearchUserResponse(emptyList()))
        } else {
            users
        }
    }

    private suspend fun getUser(username: String): Response<User> {
        return RetrofitHelper.apiService.getUser(username)
    }

    companion object {
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(): UserRepositoryImpl {
            if (INSTANCE == null) {
                INSTANCE = UserRepositoryImpl()
            }
            return INSTANCE!!
        }
    }
}