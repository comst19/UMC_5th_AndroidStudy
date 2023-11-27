package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comst.flocloneapp.databinding.ItemFloChartBinding
import com.comst.flocloneapp.model.network.FloChartSongs

class LookFragmentFloChartAdapter : ListAdapter<FloChartSongs, LookFragmentFloChartAdapter.FloChartSongViewHolder>(DiffCallback) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<FloChartSongs>(){
            override fun areItemsTheSame(oldItem: FloChartSongs, newItem: FloChartSongs): Boolean {
                return oldItem.songIdx == newItem.songIdx

            }

            override fun areContentsTheSame(oldItem: FloChartSongs, newItem: FloChartSongs): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class FloChartSongViewHolder(private val binding : ItemFloChartBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(chartSong : FloChartSongs){
            Glide.with(binding.itemSongImgIv.context)
                .load(chartSong.coverImgUrl)
                .placeholder(null)
                .into(binding.itemSongImgIv)
            binding.itemSongTitleTv.text = chartSong.title
            binding.itemSongSingerTv.text = chartSong.singer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloChartSongViewHolder {
        val binding = ItemFloChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FloChartSongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FloChartSongViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}