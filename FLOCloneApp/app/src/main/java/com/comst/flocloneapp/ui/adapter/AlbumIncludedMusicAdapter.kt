package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemAlbumIncludedSongBinding
import com.comst.flocloneapp.listener.PlayMusicListener
import com.comst.flocloneapp.model.local.SongEntity

class AlbumIncludedMusicAdapter(private val listener : PlayMusicListener) : ListAdapter<SongEntity, AlbumIncludedMusicAdapter.AlbumIncludedMusicListViewHolder>(
    DiffCallback
) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<SongEntity>(){
            override fun areItemsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean {
                return oldItem == newItem
            }

        }
    }


    inner class AlbumIncludedMusicListViewHolder(private val binding : ItemAlbumIncludedSongBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(albumIncludeMusic : SongEntity){

            binding.albumSongIndex.text = albumIncludeMusic.id.toString()
            binding.musicArtistTextView.text = albumIncludeMusic.singer
            binding.musicTitleTextView.text = albumIncludeMusic.title

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