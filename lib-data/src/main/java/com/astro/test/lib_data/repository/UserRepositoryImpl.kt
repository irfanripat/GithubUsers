package com.astro.test.lib_data.repository

import com.astro.test.lib_data.model.SearchUserResponse
import com.astro.test.lib_data.model.User
import com.astro.test.lib_data.network.RetrofitHelper
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UserRepositoryImpl : UserRepository {

    override suspend fun searchUsers(query: String): Response<SearchUserResponse> {
        return try {
            val response = RetrofitHelper.apiService.searchUsers("$query in:name")
            if (response.isSuccessful) {
                val usersWithName = response.body()?.items?.map {
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
                response
            }
        } catch (e: UnknownHostException) {
            Response.success(SearchUserResponse(emptyList()))
        } catch (e: SocketTimeoutException) {
            Response.success(SearchUserResponse(emptyList()))
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