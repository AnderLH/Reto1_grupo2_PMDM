package com.grupo2.speakr.ui.songs.favourite

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.data.User
import com.grupo2.speakr.data.repository.CommonSongRepository
import com.grupo2.speakr.data.repository.remote.RemoteSongDataSource
import com.grupo2.speakr.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(private val songRepository: CommonSongRepository) : ViewModel() {
    private val _items = MutableLiveData<Resource<List<Song>>?>()
    private var originalSongs: List<Song> = emptyList()

    val items: MutableLiveData<Resource<List<Song>>?> get() = _items

    private val _delete = MutableLiveData<Resource<Int>>()

    val delete : MutableLiveData<Resource<Int>> get() = _delete

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

    fun deleteFav(song: Song, context: Context) {
        val id: Int = song.id
         // You need to implement this function to get the song's title
        viewModelScope.launch {
            _delete.value = deleteFavouriteSong(id)
            Log.i("delete", "ok")
        }

        // Show a Toast message indicating the deletion
        val toastMessage = "${song?.title}, Deleted from favorites"
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()

        updateSongList()
    }

    private suspend fun getSongsFromRepository(): Resource<List<Song>>? {
        return withContext(Dispatchers.IO) {
            var id : Int = 3
            Log.i("info", songRepository.getFavouriteSongsFromUser().toString())
            songRepository.getFavouriteSongsFromUser()


        }
    }

    private suspend fun deleteFavouriteSong(idSong: Int): Resource<Int>{
        return withContext(Dispatchers.IO){
            songRepository.deleteFavouriteForUser(idSong)
        }
    }
}

class FavouriteViewModelFactory(
    private val songRepository : RemoteSongDataSource
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return FavouriteViewModel(songRepository) as T
    }
}