package com.grupo2.speakr.ui.songs.favourite

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.data.repository.remote.RemoteSongDataSource
import com.grupo2.speakr.databinding.ActivitySongsListBinding
import com.grupo2.speakr.ui.songs.all.SongAdapter
import com.grupo2.speakr.ui.songs.all.SongViewModel
import com.grupo2.speakr.ui.songs.all.SongsViewModelFactory
import com.grupo2.speakr.utils.Resource

class FavouriteListActivity : ComponentActivity() {

    private lateinit var songListAdapter: FavouriteListAdapter

    private val songRepository = RemoteSongDataSource()

    private val viewModel: FavouriteViewModel by viewModels { FavouriteViewModelFactory(songRepository) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("recorrido", "2")

        // a la lista de empleados le incluyo el adapter de empleado
        songListAdapter = FavouriteListAdapter(::onSongsListClickItem)
        binding.songsList.adapter = songListAdapter

        viewModel.items.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.e("PruebasDia1", "ha ocurrido un cambio en la lista")

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (it.data != null) {
                        songListAdapter.submitList(it.data)
                    }
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Resource.Status.LOADING -> {
                    // de momento
                }
            }

            //
        })

        // Agrega un TextWatcher al campo de búsqueda para filtrar las canciones
        val searchEditText = binding.searchSong

        searchEditText.addTextChangedListener(/* watcher = */ object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No es necesario implementar esto
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                viewModel.filterSongs(query)
            }
        })
    }

    private fun onSongsListClickItem(song: Song) {
        Log.i("PRUEBA1", song.url)
        this.openYoutubeLink(song.url)
    }

    private fun openYoutubeLink(youtubeURL: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL))
        try {
            this.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            this.startActivity(intentBrowser)
        }

    }
}