package com.comst.flocloneapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.comst.flocloneapp.AlbumDetailFragment
import com.comst.flocloneapp.AlbumIncludedSongsFragment
import com.comst.flocloneapp.AlbumVideoFragment

class AlbumFragmentViewPagerAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlbumIncludedSongsFragment()
            1 -> AlbumDetailFragment()
            2 -> AlbumVideoFragment()
            else -> AlbumIncludedSongsFragment()
        }
    }
}