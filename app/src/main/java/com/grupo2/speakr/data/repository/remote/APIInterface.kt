package com.grupo2.speakr.data.repository.remote

import com.grupo2.speakr.data.Song
import retrofit2.Response
import retrofit2.http.GET

interface APIInterface {
    @GET("api/songs")
    suspend fun getSongs(): Response<List<Song>>
}