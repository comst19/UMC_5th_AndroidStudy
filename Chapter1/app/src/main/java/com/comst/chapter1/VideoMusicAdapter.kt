package com.comst.chapter1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.chapter1.databinding.ItemVideoMusicBinding
import com.comst.chapter1.model.TodayMusic
import com.comst.chapter1.model.VideoMusic

class VideoMusicAdapter : ListAdapter<VideoMusic, VideoMusicAdapter.VideoMusicListViewHolder>(DiffCallback) {


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

    inner class VideoMusicListViewHolder(private val binding : ItemVideoMusicBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoMusicListViewHolder {
        val binding = ItemVideoMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoMusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoMusicListViewHolder, position: Int) {

    }


}