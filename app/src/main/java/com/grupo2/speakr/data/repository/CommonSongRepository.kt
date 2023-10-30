package com.grupo2.speakr.data.repository

import com.grupo2.speakr.data.Song
import com.grupo2.speakr.utils.Resource

interface CommonSongRepository {
    suspend fun getSongs() : Resource<List<Song>>
    suspend fun getFavouriteSongs(id: Int): Resource<List<Song>>
}