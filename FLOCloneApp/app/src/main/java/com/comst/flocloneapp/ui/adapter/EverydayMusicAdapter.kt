package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemEverydayMusicBinding
import com.comst.flocloneapp.model.EverydayMusic

class EverydayMusicAdapter : ListAdapter<EverydayMusic, EverydayMusicAdapter.EveryDayMusicListViewHolder>(
    DiffCallback
) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<EverydayMusic>(){
            override fun areItemsTheSame(oldItem: EverydayMusic, newItem: EverydayMusic): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: EverydayMusic, newItem: EverydayMusic): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class EveryDayMusicListViewHolder(private val binding : ItemEverydayMusicBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(everydayMusic : EverydayMusic){
            binding.everydayMusicTitleTextView.text = everydayMusic.musicName
            binding.everydayMusicArtist.text = everydayMusic.artist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EveryDayMusicListViewHolder {
        val binding = ItemEverydayMusicBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return EveryDayMusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EveryDayMusicListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}