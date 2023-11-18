package com.comst.flocloneapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.comst.flocloneapp.ui.LockerLikeSongFragment
import com.comst.flocloneapp.ui.LockerMusicFileFragment
import com.comst.flocloneapp.ui.LockerSavedAlbumFragment
import com.comst.flocloneapp.ui.LockerSavedSongFragment

class LockerViewPagerAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LockerSavedSongFragment()
            1 -> LockerMusicFileFragment()
            2 -> LockerLikeSongFragment()
            3 -> LockerSavedAlbumFragment()
            else -> LockerSavedSongFragment()
        }
    }
}