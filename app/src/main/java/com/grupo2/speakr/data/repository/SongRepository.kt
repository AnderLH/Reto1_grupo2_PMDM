package com.grupo2.speakr.data.repository

class SongRepository (private val repository : CommonSongRepository) {

    suspend fun getSongs() = repository.getSongs()
}