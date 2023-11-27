package com.comst19.chapter09mission

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comst19.chapter09mission.Coins.CoinlInfo
import com.comst19.chapter09mission.databinding.CoinListBinding

class CoinAdapter(private var coinlist : MutableList<CoinlInfo>) : RecyclerView.Adapter<CoinAdapter.MyViewHolder>(){


    inner class MyViewHolder(private val binding : CoinListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(coin : CoinlInfo){

            binding.coinName.text = coin.name
            binding.coinPriceRange.text = "${coin.coinDetailInfo.min_price} ~ ${coin.coinDetailInfo.max_price}"
            //binding.coinName.text = coin.coinName
            //binding.coinPrice.text = coin.coinDetailInfo.price
            //binding.date.text = coin.coinDetailInfo.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(coinlist[position])
    }

    override fun getItemCount(): Int {
        return coinlist.size
    }
}