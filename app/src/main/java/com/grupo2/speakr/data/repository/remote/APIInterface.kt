package com.grupo2.speakr.data.repository.remote

import com.grupo2.speakr.data.Song
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIInterface {
    @GET("songs")
    suspend fun getSongs(): Response<List<Song>>

    @GET("songs/{id}/favourites")
    suspend fun getFavouriteSongs(@Path("id") id : Int): Response<List<Song>>
}