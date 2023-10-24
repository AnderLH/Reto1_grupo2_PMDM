package com.grupo2.speakr.data.repository.remote

import com.grupo2.speakr.data.Song
import com.grupo2.speakr.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {
    @GET("songs")
    suspend fun getSongs(): Response<List<Song>>

    @POST("users")
    suspend fun createUser(@Body user: User) : Response<Int>
}