package com.comst.chapter1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.chapter1.databinding.ItemTodayMusicBinding
import com.comst.chapter1.model.TodayMusic

class TodayMusicAdapter : ListAdapter<TodayMusic,TodayMusicAdapter.TodayMusicListViewHolder>(DiffCallback) {


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

    inner class TodayMusicListViewHolder(private val binding : ItemTodayMusicBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMusicListViewHolder {
        val binding = ItemTodayMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodayMusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayMusicListViewHolder, position: Int) {

    }
}