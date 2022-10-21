package com.astro.test.irfan.data.network

import com.astro.test.irfan.data.model.SearchUserResponse
import com.astro.test.irfan.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): Response<SearchUserResponse>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>
}