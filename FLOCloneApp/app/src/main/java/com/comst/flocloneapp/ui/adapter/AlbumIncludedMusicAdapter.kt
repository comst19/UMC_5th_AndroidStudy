package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemAlbumIncludedSongBinding
import com.comst.flocloneapp.listener.PlayMusicListener
import com.comst.flocloneapp.model.AlbumIncludeMusic

class AlbumIncludedMusicAdapter(private val listener : PlayMusicListener) : ListAdapter<AlbumIncludeMusic, AlbumIncludedMusicAdapter.AlbumIncludedMusicListViewHolder>(
    DiffCallback
) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<AlbumIncludeMusic>(){
            override fun areItemsTheSame(oldItem: AlbumIncludeMusic, newItem: AlbumIncludeMusic): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: AlbumIncludeMusic, newItem: AlbumIncludeMusic): Boolean {
                return oldItem == newItem
            }

        }
    }


    inner class AlbumIncludedMusicListViewHolder(private val binding : ItemAlbumIncludedSongBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(albumIncludeMusic : AlbumIncludeMusic){

            binding.albumSongIndex.text = albumIncludeMusic.id.toString()
            binding.musicArtistTextView.text = albumIncludeMusic.artist
            binding.musicTitleTextView.text = albumIncludeMusic.musicName
            if (albumIncludeMusic.isTitleMusic){
                binding.isTitleTextView.visibility = View.VISIBLE
            }else{
                binding.isTitleTextView.visibility = View.GONE
            }

            binding.albumIncludedSongPlay.setOnClickListener {
                listener.albumIncludedSongsPlay(albumIncludeMusic)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumIncludedMusicListViewHolder {
        val binding = ItemAlbumIncludedSongBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return AlbumIncludedMusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumIncludedMusicListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}