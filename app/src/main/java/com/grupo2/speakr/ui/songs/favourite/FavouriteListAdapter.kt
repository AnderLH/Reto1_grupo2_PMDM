package com.grupo2.speakr.ui.songs.favourite

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.databinding.ItemSongsBinding
import com.grupo2.speakr.ui.songs.all.SongAdapter

class FavouriteListAdapter(
        private val onClickListener: (Song) -> Unit
    ) : ListAdapter<Song, FavouriteListAdapter.SongViewHolder>(FavouriteDiffCallBack()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
            val binding = ItemSongsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SongViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
            val song = getItem(position)
            holder.bind(song)
            holder.itemView.setOnClickListener {
                Log.e("statusInfo", "songAdapter " + song.id.toString())
                onClickListener(song)
            }
        }

        inner class SongViewHolder(private val binding: ItemSongsBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(song: Song) {
                binding.songName.text = song.title
                binding.songAuthor.text = song.author
                // Update other views as needed for your Song object
            }
        }

        class FavouriteDiffCallBack : DiffUtil.ItemCallback<Song>() {

            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return (oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.author == newItem.author && oldItem.url == newItem.url)
                // You can add more fields as needed to compare
            }
        }
    }