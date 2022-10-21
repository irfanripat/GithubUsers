package com.astro.test.irfan.data.repository

import com.astro.test.irfan.data.model.SearchUserResponse
import com.astro.test.irfan.data.model.User
import com.astro.test.irfan.data.network.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepositoryImpl : UserRepository {

    override suspend fun searchUsers(query: String): Response<SearchUserResponse> {
        val users = RetrofitHelper.apiService.searchUsers(query)
        if (users.isSuccessful) {
            val usersWithName = users.body()?.items?.map {
                val user = RetrofitHelper.apiService.getUser(it.login)
                if (user.isSuccessful) {
                    user.body()
                } else {
                    it
                }
            }
            return usersWithName?.let {
                val userss = it.map {
                    it as User
                }
                Response.success(SearchUserResponse(userss))
            } ?: Response.success(SearchUserResponse(emptyList()))
        } else {
            throw Exception("Error")
        }
    }

    override suspend fun getUser(username: String): Response<User> {
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