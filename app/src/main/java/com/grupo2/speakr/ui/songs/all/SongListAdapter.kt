package com.grupo2.speakr.ui.songs.all

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.databinding.ItemSongsBinding
import com.bumptech.glide.Glide
import com.grupo2.speakr.R
import java.util.regex.Pattern

class SongAdapter(
    private val onClickListener: (Song) -> Unit
) : ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()) {

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

            // Cargar la miniatura de YouTube usando Glide
            Glide.with(binding.root)
                .load(getYouTubeThumbnailUrl(song.url))
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.songImage)
        }
    }

    class SongDiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return (oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.author == newItem.author && oldItem.url == newItem.url)
            // You can add more fields as needed to compare
        }
    }
    private fun getYouTubeThumbnailUrl(videoUrl: String): String {
        val videoId = extractVideoIdFromUrl(videoUrl)
        return "https://img.youtube.com/vi/$videoId/default.jpg"
    }

    private fun extractVideoIdFromUrl(videoUrl: String): String {
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%\u200C\u200B2F|%2Fv%\u200C\u200B|e%\u200C\u200B|%2Fwatch%\u200C\u200B?feature=player_embedded&v=|%2Fe%\u200C\u200B|\\/embed\\/|%2Fwatch?feature=player_embedded&v=)([^#\\&\\?\\n]*)(?:[\\w!\\?\\-]+)?"
        val patternMatcher = Pattern.compile(pattern)
        val matcher = patternMatcher.matcher(videoUrl)

        return if (matcher.find()) {
            matcher.group()
        } else {
            // Si no se puede encontrar el ID del video, puedes manejarlo como prefieras
            // En este ejemplo, simplemente devolvemos la URL completa
            videoUrl
        }
    }
}