package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemLockerAlbumBinding
import com.comst.flocloneapp.databinding.ItemTodayMusicBinding
import com.comst.flocloneapp.model.AlbumEntity

class LockerSavedAlbumAdapter :
    ListAdapter<AlbumEntity, LockerSavedAlbumAdapter.LockerSavedAlbumListViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class LockerSavedAlbumListViewHolder(private val binding: ItemLockerAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumEntity) {
            binding.itemAlbumImgIv.setImageResource(album.coverImg!!)
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerSavedAlbumListViewHolder {
        val binding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LockerSavedAlbumListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerSavedAlbumListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}