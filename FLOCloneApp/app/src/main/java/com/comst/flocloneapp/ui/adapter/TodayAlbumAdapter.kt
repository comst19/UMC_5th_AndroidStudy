package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemTodayMusicBinding
import com.comst.flocloneapp.model.AlbumEntity
import com.comst.flocloneapp.model.TodayMusic

class TodayAlbumAdapter  : ListAdapter<AlbumEntity, TodayAlbumAdapter.TodayAlbumListViewHolder>(DiffCallback){
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
        fun bind(todayMusic : AlbumEntity){
            binding.todayMusicTitleTextView.text = todayMusic.title
            binding.todayMusicArtistTextView.text = todayMusic.singer
            binding.todayMusicImageView.setImageResource(todayMusic.coverImg!!)
            binding.root.setOnClickListener {
                //listener.goAlbumFragment(adapterPosition)
            }
            binding.todayPlayMusic.setOnClickListener {
                //listener.playMusic(todayMusic)
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