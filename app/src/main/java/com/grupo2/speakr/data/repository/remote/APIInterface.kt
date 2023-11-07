package com.grupo2.speakr.data.repository.remote

import com.grupo2.speakr.data.LoginUser
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.data.User
import com.grupo2.speakr.data.repository.AuthenticationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {
    @GET("songs")
    suspend fun getSongs(): Response<List<Song>>
  
    @POST("users/auth/signup")
    suspend fun createUser(@Body user: User) : Response<Int>

    @POST("users/auth/login")
    suspend fun loginUser(@Body user: LoginUser) : Response<AuthenticationResponse>

    @GET("users/{id}/favourites")
    suspend fun getFavouriteSongs(@Path("id") id : Int): Response<List<Song>>

    @GET("users/favourites/me")
    suspend fun getFavouriteSongsForUser(): Response<List<Song>>
}