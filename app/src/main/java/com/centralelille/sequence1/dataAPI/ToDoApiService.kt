package com.centralelille.sequence1.dataAPI

import com.centralelille.sequence1.dataAPI.model.*
import retrofit2.http.*

interface ToDoApiService {

    @POST("authenticate")
    suspend fun authenticate (@Query("user") pseudo: String, @Query("password") pass: String): AuthenticateResponse

    @POST("users")
    suspend fun postNewUser(@Query("pseudo") newPseudo: String, @Query("pass") newPass: String, @Query("hash") hash : String): UserResponse

    @GET("lists")
    suspend fun getCurrentUserLists (@Query("hash") hash: String): ListsResponse

    @POST("lists")
    suspend fun insertListForCurrentUser (@Query("label") label: String, @Query("hash") hash: String): ListResponse

    @GET("lists/{listId}/items")
    suspend fun getListOfCurrentUserById(@Path("listId")listId: Int, @Query("hash") hash: String): ItemsResponse

    @POST("lists/{listId}/items")
    suspend fun insertItem(@Path("listId") listId: Int, @Query("label") label: String, @Query("hash") hash: String): ItemResponse

    @PUT("lists/{listId}/items/{itemId}")
    suspend fun setItemState(@Path("listId") listId: Int, @Path("itemId") itemId: Int, @Query("check") check: Int, @Query("hash") hash: String): ItemResponse
}