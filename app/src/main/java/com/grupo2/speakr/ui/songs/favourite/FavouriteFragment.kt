package com.grupo2.speakr.ui.songs.favourite

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.grupo2.speakr.R
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.data.repository.remote.RemoteSongDataSource
import com.grupo2.speakr.databinding.FragmentSongsListBinding
import com.grupo2.speakr.utils.Resource

class FavouriteFragment : Fragment() {

    private lateinit var favouriteListAdapter: FavouriteListAdapter
    private val songRepository = RemoteSongDataSource()
    private val viewModel: FavouriteViewModel by viewModels { FavouriteViewModelFactory(songRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSongsListBinding.inflate(inflater, container, false)
        val view = binding.root

        val filterTypes = listOf<String>("Titulo", "Autor")
        val autoComplete: AutoCompleteTextView = binding.autoCompleteTxt
        val adapter = context?.let { ArrayAdapter(it, R.layout.filter_menu_item, filterTypes) }

        autoComplete.setAdapter(adapter)
        autoComplete.setOnClickListener{
            val emptyString = ""
            binding.searchSong.setText(emptyString).toString()
            AdapterView.OnItemClickListener{
                    adapterView, view, i, l ->
                val selectedItem = adapterView.getItemAtPosition(i)
                //Toast.makeText(this, "Item: $selectedItem", Toast.LENGTH_SHORT).show()
            }
        }

        favouriteListAdapter = FavouriteListAdapter(::onSongsListClickItem, ::onImageButtonClick)
        binding.songsList.adapter = favouriteListAdapter

        viewModel.items.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        if (it.data != null) {
                            favouriteListAdapter.submitList(it.data)
                        }
                    }

                    Resource.Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    Resource.Status.LOADING -> {
                    }
                }
            }
        })

        val searchEditText = binding.searchSong
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if(binding.autoCompleteTxt.text.toString() == filterTypes[0]) {
                    viewModel.filterSongsTitle(query)
                }else {
                    viewModel.filterSongsAuthor(query)
                }

            }
        })

        return view
    }

    private fun onSongsListClickItem(song: Song) {
        viewModel.addView(song.id)
        openYoutubeLink(song.url)
    }

    private fun openYoutubeLink(youtubeURL: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL))
        try {
            requireContext().startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            requireContext().startActivity(intentBrowser)
        }
    }

    private fun onImageButtonClick(song: Song) {
        // Handle the click event of the ImageButton here
        Log.i("Image", song.favorite.toString())
        val id : Int = song.id
        viewModel.deleteFav(song, requireContext())

        //viewModel.createFav(id)

        // You can access the details of the `song` and perform the desired action.
        // For example, you can change the state of the ImageButton to yellow.
    }
}