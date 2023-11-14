package com.comst.flocloneapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.databinding.FragmentLockerSavedSongBinding
import com.comst.flocloneapp.listener.SavedMusicListener
import com.comst.flocloneapp.model.LockerSavedMusic
import com.comst.flocloneapp.model.SongEntity
import com.comst.flocloneapp.ui.adapter.LockerSavedMusicAdapter
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LockerSavedSongFragment : Fragment(), SavedMusicListener {


    private var _binding : FragmentLockerSavedSongBinding? = null
    private val binding get() = _binding!!

    private val savedSongAdapter = LockerSavedMusicAdapter(this)

    private var savedMusicList = mutableListOf<SongEntity>()
    private val miniPlayerViewModel : MiniPlayerViewModel by activityViewModels()

    lateinit var songDB : SongDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLockerSavedSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        initView()
        return binding.root
    }

    fun initView(){

        getSavedSongList()


        with(binding){
            //todayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            lockerSavedSongRecyclerView.adapter = savedSongAdapter
        }

    }

    private fun getSavedSongList(){
        CoroutineScope(Dispatchers.IO).launch {
            savedMusicList.addAll(songDB.SongDao().getSongs())
            withContext(Dispatchers.Main){
                savedSongAdapter.submitList(savedMusicList)
            }
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

    // Room 내용은 따로 안 바꿈
    override fun deleteSavedSong(position: Int) {
        val updatedList = savedMusicList.toMutableList()
        updatedList.removeAt(position)
        savedSongAdapter.submitList(updatedList)
        savedMusicList = updatedList
    }
}