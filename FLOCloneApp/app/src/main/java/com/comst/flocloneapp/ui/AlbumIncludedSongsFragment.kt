package com.comst.flocloneapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.ui.adapter.AlbumIncludedMusicAdapter
import com.comst.flocloneapp.databinding.FragmentAlbumIncludedSongsBinding
import com.comst.flocloneapp.listener.PlayMusicListener
import com.comst.flocloneapp.model.local.SongEntity
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlbumIncludedSongsFragment : Fragment(), PlayMusicListener {

    private var _binding : FragmentAlbumIncludedSongsBinding? = null
    private val binding get() = _binding!!

    private val albumIncludedMusicAdapter = AlbumIncludedMusicAdapter(this)
    //private val albumIncludeMusicList = mutableListOf<AlbumIncludeMusic>()
    private var albumIncludeSongList = mutableListOf<SongEntity>()
    private val miniPlayerViewModel : MiniPlayerViewModel by activityViewModels()

    lateinit var songDB : SongDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAlbumIncludedSongsBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        initView()
        return binding.root
    }

    private fun initView(){

        getAlbumIncludeSongs()

        with(binding){

            //albumIncludeMusicDummy()

            albumIncludedSongRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            albumIncludedSongRecyclerView.adapter = albumIncludedMusicAdapter
            //albumIncludedMusicAdapter.submitList(albumIncludeMusicList)
        }

    }

    private fun getAlbumIncludeSongs(){


        val albumId = miniPlayerViewModel.albumId.value!!

        CoroutineScope(Dispatchers.IO).launch {

            albumIncludeSongList = songDB.SongDao().getIncludeSong(albumId).toMutableList()
            albumIncludedMusicAdapter.submitList(albumIncludeSongList)
        }
    }

    /*
    private fun albumIncludeMusicDummy(){

        for (i in 1..6){
            if (i == 2){
                val dummy = AlbumIncludeMusic(i,"LILAC $i", true, "아이유 (IU)")
                albumIncludeMusicList.add(dummy)
            }else{
                val dummy = AlbumIncludeMusic(i,"LILAC $i", false, "아이유 (IU)")
                albumIncludeMusicList.add(dummy)
            }

        }
    }
    */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun albumIncludedSongsPlay(albumIncludeMusic: SongEntity) {
        MusicPlayServiceUtil.stopService(requireContext())
        miniPlayerViewModel.resetViewModel()
        miniPlayerViewModel.updateMiniPlayerUI(albumIncludeMusic.title, albumIncludeMusic.singer)
        miniPlayerViewModel.musicPlay.value = true
        miniPlayerViewModel.isMusicTimeOver.value = false
        miniPlayerViewModel.musicPlayOrPause()
    }
}
