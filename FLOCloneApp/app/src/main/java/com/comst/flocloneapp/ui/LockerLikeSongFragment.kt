package com.comst.flocloneapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.databinding.FragmentLockerLikeSongBinding
import com.comst.flocloneapp.databinding.FragmentLockerSavedSongBinding
import com.comst.flocloneapp.listener.SavedMusicListener
import com.comst.flocloneapp.model.SongEntity
import com.comst.flocloneapp.ui.adapter.LockerLikeSongAdapter
import com.comst.flocloneapp.ui.adapter.LockerSavedMusicAdapter
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LockerLikeSongFragment : Fragment(), SavedMusicListener {

    private var _binding : FragmentLockerLikeSongBinding? = null
    private val binding get() = _binding!!

    private var likeSongList = arrayListOf<SongEntity>()
    private val miniPlayerViewModel : MiniPlayerViewModel by activityViewModels()

    private val savedSongAdapter = LockerLikeSongAdapter(this)

    lateinit var songDB : SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerLikeSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        initView()
        return binding.root
    }

    private fun getLikeSongList(){
        CoroutineScope(Dispatchers.IO).launch {
            likeSongList.addAll(songDB.SongDao().getLikedSongs())
            withContext(Dispatchers.Main){
                savedSongAdapter.submitList(likeSongList)
            }
        }
    }

    fun initView(){
        getLikeSongList()

        with(binding){
            //todayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            lockerLikedSongRecyclerView.adapter = savedSongAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun savedSongsPlay(savedMusic: SongEntity) {
        MusicPlayServiceUtil.stopService(requireContext())
        miniPlayerViewModel.resetViewModel()
        miniPlayerViewModel.updateMiniPlayerUI(savedMusic.title,savedMusic.singer)
        miniPlayerViewModel.musicPlay.value = true
        miniPlayerViewModel.isMusicTimeOver.value = false
        miniPlayerViewModel.musicPlayOrPause()
    }

    override fun deleteSavedSong(position: Int) {
        val updatedList = likeSongList.toMutableList()
        updatedList.removeAt(position)
        savedSongAdapter.submitList(likeSongList)
    }
}