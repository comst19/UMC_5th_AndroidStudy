package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.ItemSavedSongBinding
import com.comst.flocloneapp.listener.SavedMusicListener
import com.comst.flocloneapp.model.LockerSavedMusic

class LockerSavedMusicAdapter(private val listener : SavedMusicListener) : ListAdapter<LockerSavedMusic,LockerSavedMusicAdapter.LockerSavedListViewHolder>(DiffCallback) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<LockerSavedMusic>(){
            override fun areItemsTheSame(oldItem: LockerSavedMusic, newItem: LockerSavedMusic): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: LockerSavedMusic, newItem: LockerSavedMusic): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class LockerSavedListViewHolder(private val binding : ItemSavedSongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(savedMusic : LockerSavedMusic){
            binding.itemSongImgIv.setImageResource(savedMusic.musicImg)
            binding.itemSongSingerTv.text = savedMusic.artist
            binding.itemSongTitleTv.text = savedMusic.musicName
            savedMusic.switchOnOff = savedMusic.switchOnOff


            binding.itemSongSwitch.setOnClickListener {
                savedMusic.switchOnOff = !savedMusic.switchOnOff
            }

            binding.itemSongPlayIv.setOnClickListener {
                listener.savedSongsPlay(savedMusic)
            }

            binding.itemSongMoreIv.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.menuInflater.inflate(R.menu.saved_song_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.deleteSavedSong -> {
                            listener.deleteSavedSong(adapterPosition)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerSavedListViewHolder {
        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LockerSavedListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerSavedListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}