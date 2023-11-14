package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemTodayMusicBinding
import com.comst.flocloneapp.listener.ItemTodayMusicListener
import com.comst.flocloneapp.model.AlbumEntity

class TodayAlbumAdapter(private val listener : ItemTodayMusicListener)  : ListAdapter<AlbumEntity, TodayAlbumAdapter.TodayAlbumListViewHolder>(DiffCallback){
    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<AlbumEntity>(){
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class TodayAlbumListViewHolder(private val binding : ItemTodayMusicBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album : AlbumEntity){
            binding.todayMusicTitleTextView.text = album.title
            binding.todayMusicArtistTextView.text = album.singer
            binding.todayMusicImageView.setImageResource(album.coverImg!!)
            binding.root.setOnClickListener {
                listener.goAlbumFragment(adapterPosition)
            }
            binding.todayPlayMusic.setOnClickListener {
                listener.playMusic(album.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayAlbumListViewHolder {
        val binding = ItemTodayMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodayAlbumListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayAlbumListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}