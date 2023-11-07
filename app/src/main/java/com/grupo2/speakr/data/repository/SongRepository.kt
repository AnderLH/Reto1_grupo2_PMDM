package com.grupo2.speakr.data.repository

import com.grupo2.speakr.utils.Resource

class SongRepository (private val repository : CommonSongRepository) {

    suspend fun getSongs() = repository.getSongs()

    suspend fun getFavouriteSongs(id : Int) = repository.getFavouriteSongs(id)

    suspend fun getFavouriteSongsFromUser() = repository.getFavouriteSongsFromUser()

    suspend fun createFavouriteForUser(idSong : Int) = repository.createFavouriteForUser(idSong)
}