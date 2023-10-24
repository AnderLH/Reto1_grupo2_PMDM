package com.grupo2.speakr.ui.songs.all

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.activity.viewModels

import com.grupo2.speakr.data.repository.remote.RemoteSongDataSource
import com.grupo2.speakr.databinding.ActivitySongsListBinding
import com.grupo2.speakr.utils.Resource

class SongListActivity : ComponentActivity() {

    private lateinit var songListAdapter: SongListAdapter

    private val songRepository = RemoteSongDataSource()

    private val viewModel: SongViewModel by viewModels { SongsViewModelFactory(songRepository) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.items.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.e("PruebasDia1", "ha ocurrido un cambio en la lista")

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
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
    }

}