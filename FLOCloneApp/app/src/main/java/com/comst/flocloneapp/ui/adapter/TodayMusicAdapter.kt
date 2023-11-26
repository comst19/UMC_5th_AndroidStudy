package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemTodayMusicBinding
import com.comst.flocloneapp.listener.ItemTodayMusicListener
import com.comst.flocloneapp.model.local.TodayMusic

class TodayMusicAdapter(private val listener : ItemTodayMusicListener) : ListAdapter<TodayMusic, TodayMusicAdapter.TodayMusicListViewHolder>(
    DiffCallback
) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<TodayMusic>(){
            override fun areItemsTheSame(oldItem: TodayMusic, newItem: TodayMusic): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: TodayMusic, newItem: TodayMusic): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class TodayMusicListViewHolder(private val binding : ItemTodayMusicBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(todayMusic : TodayMusic){
            binding.todayMusicTitleTextView.text = todayMusic.musicName
            binding.todayMusicArtistTextView.text = todayMusic.artist
            binding.root.setOnClickListener {
                listener.goAlbumFragment(adapterPosition)
            }
            binding.todayPlayMusic.setOnClickListener {
                //listener.playMusic(todayMusic)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMusicListViewHolder {
        val binding = ItemTodayMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodayMusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayMusicListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}