package com.centralelille.sequence1.dataAPI

import com.centralelille.sequence1.dataAPI.model.Users
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {
    val BASE_URL = "http://tomnab.fr/todo-api"

    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ToDoApiService::class.java)

    suspend fun getUsersFromApi(): List<Users> = service.getUsers()
}