package com.centralelille.sequence1.dataAPI

import com.centralelille.sequence1.dataAPI.model.Users
import retrofit2.http.GET
import retrofit2.http.Headers

interface ToDoApiService {
    @Headers(
        "hash",
        "3f42b18b7f71498b166d1662848a5bec")
    @GET("/users")
    suspend fun getUsers(): List<Users>
}