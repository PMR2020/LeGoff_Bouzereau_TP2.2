package com.centralelille.sequence1.dataAPI

import android.util.Log
import com.centralelille.sequence1.dataAPI.model.*
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object DataProvider {
    val BASE_URL = "http://tomnab.fr/todo-api/"

    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ToDoApiService::class.java)

    suspend fun authenticate(pseudo:  String, password: String): AuthenticateResponse = service.authenticate(pseudo, password)

    suspend fun insertNewUser(newPseudo: String, newPass: String, hash: String): UserResponse = service.postNewUser(newPseudo, newPass, hash)


    suspend fun getCurrentUserLists(hash: String): ListsResponse = service.getCurrentUserLists(hash)

    suspend fun insertListForCurrentUser(label: String, hash: String): ListResponse = service.insertListForCurrentUser(label, hash)

    suspend fun getItemsFromListById(listeId: Int, hash: String): ItemsResponse = service.getListOfCurrentUserById(listeId, hash)

    suspend fun insertNewItem(listeId: Int, label: String, hash: String): ItemResponse = service.insertItem(listeId, label, hash)

    suspend fun setItemState(listId: Int, itemId: Int, checkState: Int, hash: String): ItemResponse = service.setItemState(listId, itemId, checkState, hash)
}