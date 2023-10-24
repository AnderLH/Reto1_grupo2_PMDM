package com.grupo2.speakr.ui.songs.all

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grupo2.speakr.data.Song
import com.grupo2.speakr.databinding.ItemSongsBinding

class SongListAdapter (
    private val onClickListener: (Song) -> Unit
): ListAdapter<Song, SongListAdapter.DepartmentViewHolder>(DepartmentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val binding =
            ItemSongsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DepartmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        val department = getItem(position)
        holder.bind(department)
        holder.itemView.setOnClickListener(){
            onClickListener(department)
            Log.e("statusInfo", "departmentAdapter " + department.id.toString())

        }
    }

    inner class DepartmentViewHolder(private val binding: ItemSongsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.textViewTitle.text = song.title
            binding.textViewSubtitle1.text = song.author
            binding.textViewSubtitle2.text = song.url
        }
    }

    class DepartmentDiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return (oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.author == newItem.author && oldItem.url == newItem.url)
            // return oldItem == newItem
        }

    }
}