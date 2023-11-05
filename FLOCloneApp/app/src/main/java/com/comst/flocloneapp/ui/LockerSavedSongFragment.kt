package com.comst.flocloneapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.FragmentLockerSavedSongBinding
import com.comst.flocloneapp.listener.SavedMusicListener
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.model.LockerSavedMusic
import com.comst.flocloneapp.ui.adapter.LockerSavedMusicAdapter
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel

class LockerSavedSongFragment : Fragment(), SavedMusicListener {


    private var _binding : FragmentLockerSavedSongBinding? = null
    private val binding get() = _binding!!

    private val savedSongAdapter = LockerSavedMusicAdapter(this)

    private var savedMusicList = mutableListOf<LockerSavedMusic>()
    private val miniPlayerViewModel : MiniPlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLockerSavedSongBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }

    fun initView(){

        savedMusicDummy()

        with(binding){
            //todayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            lockerSavedSongRecyclerView.adapter = savedSongAdapter
            savedSongAdapter.submitList(savedMusicList)
        }

    }

    private fun savedMusicDummy(){
        savedMusicList.add(LockerSavedMusic(1, R.drawable.img_album_exp2, "LILAC", "아이유 (IU)"))
        savedMusicList.add(LockerSavedMusic(2, R.drawable.img_album_exp3, "Next Level", "aespa"))
        savedMusicList.add(LockerSavedMusic(4, R.drawable.img_album_exp5, "뿜뿜", "모모랜드"))
        savedMusicList.add(LockerSavedMusic(5, R.drawable.img_album_exp6, "Weekend", "태연"))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun savedSongsPlay(savedMusic: LockerSavedMusic) {
        MusicPlayServiceUtil.stopService(requireContext())
        miniPlayerViewModel.resetViewModel()
        miniPlayerViewModel.updateMiniPlayerUI(savedMusic.musicName,savedMusic.artist)
        miniPlayerViewModel.musicPlay.value = true
        miniPlayerViewModel.isMusicTimeOver.value = false
        miniPlayerViewModel.musicPlayOrPause()
    }

    override fun deleteSavedSong(position: Int) {
        val updatedList = savedMusicList.toMutableList()
        updatedList.removeAt(position)
        savedSongAdapter.submitList(updatedList)
    }
}