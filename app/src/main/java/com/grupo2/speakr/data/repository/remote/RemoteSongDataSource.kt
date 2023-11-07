package com.grupo2.speakr.data.repository.remote

import com.grupo2.speakr.data.Song
import com.grupo2.speakr.data.repository.CommonSongRepository
import com.grupo2.speakr.utils.Resource

class RemoteSongDataSource: BaseDataSource(), CommonSongRepository {
    override suspend fun getSongs() = getResult {
        RetrofitClient.apiInterface.getSongs()
    }
    override suspend fun getFavouriteSongs(id: Int) = getResult {
        RetrofitClient.apiInterface.getFavouriteSongs(id)
    }
    override suspend fun getFavouriteSongsFromUser() = getResult {
        RetrofitClient.apiInterface.getFavouriteSongsForUser()
    }
}