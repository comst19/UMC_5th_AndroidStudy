package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemVideoMusicBinding
import com.comst.flocloneapp.model.TodayMusic
import com.comst.flocloneapp.model.VideoMusic

class VideoMusicAdapter : ListAdapter<VideoMusic, VideoMusicAdapter.VideoMusicListViewHolder>(
    DiffCallback
) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<VideoMusic>(){
            override fun areItemsTheSame(oldItem: VideoMusic, newItem: VideoMusic): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: VideoMusic, newItem: VideoMusic): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class VideoMusicListViewHolder(private val binding : ItemVideoMusicBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(videoMusic : VideoMusic){
            binding.videoMusicTitleTextView.text = videoMusic.musicName
            binding.videoMusicArtistTextView.text = videoMusic.artist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoMusicListViewHolder {
        val binding = ItemVideoMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoMusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoMusicListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}