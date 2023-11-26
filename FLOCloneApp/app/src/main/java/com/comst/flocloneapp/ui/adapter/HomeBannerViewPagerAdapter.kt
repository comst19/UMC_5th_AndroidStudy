package com.comst.flocloneapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.databinding.ItemBannerBinding
import com.comst.flocloneapp.model.local.HomeBanner

class HomeBannerViewPagerAdapter : ListAdapter<HomeBanner, HomeBannerViewPagerAdapter.HomeBannerViewHolder>(
    DiffCallback
) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<HomeBanner>(){
            override fun areItemsTheSame(oldItem: HomeBanner, newItem: HomeBanner): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: HomeBanner, newItem: HomeBanner): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class HomeBannerViewHolder(private val binding : ItemBannerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(homeBanner : HomeBanner){
            binding.titleTextView.text = homeBanner.bannerTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return 6
    }
}