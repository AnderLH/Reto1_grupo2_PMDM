package com.grupo2.speakr.ui.songs.all

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.data.repository.CommonSongRepository
import com.grupo2.speakr.data.repository.remote.RemoteSongDataSource
import com.grupo2.speakr.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongViewModel(private val songRepository: CommonSongRepository) : ViewModel() {
    private val _items = MutableLiveData<Resource<List<Song>>>()
    private var originalSongs: List<Song> = emptyList()

    val items: LiveData<Resource<List<Song>>>
        get() = _items

    init {
        updateSongList()
    }

    private fun updateSongList() {
        viewModelScope.launch {
            val repoResponse = getSongsFromRepository()
            _items.value = repoResponse
            originalSongs = repoResponse?.data.orEmpty() // Guarda la lista original
        }
    }

    fun filterSongs(query: String) {
        val currentSongs = originalSongs.toMutableList()

        // Realiza el filtrado basado en la consulta
        if (query.isNotBlank()) {
            currentSongs.retainAll { song ->
                song.title.contains(query, ignoreCase = true)
            }
        }
        Log.d("LISTA", currentSongs.size.toString())

        // Actualiza el LiveData con la lista filtrada o vacía si no hay


        _items.value = Resource.success(currentSongs)
    }


    private suspend fun getSongsFromRepository(): Resource<List<Song>>? {
        return withContext(Dispatchers.IO) {
            songRepository.getSongs()
        }
    }
}

class SongsViewModelFactory(
    private val songRepository : RemoteSongDataSource
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return SongViewModel(songRepository) as T
    }
}