package com.comst.flocloneapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.comst.flocloneapp.adapter.AlbumIncludedMusicAdapter
import com.comst.flocloneapp.databinding.FragmentAlbumIncludedSongsBinding
import com.comst.flocloneapp.listener.PlayMusicListener
import com.comst.flocloneapp.model.AlbumIncludeMusic
import com.comst.flocloneapp.viewmodel.MainViewModel


class AlbumIncludedSongsFragment : Fragment(), PlayMusicListener {

    private var _binding : FragmentAlbumIncludedSongsBinding? = null
    private val binding get() = _binding!!

    private val albumIncludedMusicAdapter = AlbumIncludedMusicAdapter(this)
    private val albumIncludeMusicList = mutableListOf<AlbumIncludeMusic>()
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAlbumIncludedSongsBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }

    private fun initView(){


        with(binding){

            albumIncludeMusicDummy()

            albumIncludedSongRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            albumIncludedSongRecyclerView.adapter = albumIncludedMusicAdapter
            albumIncludedMusicAdapter.submitList(albumIncludeMusicList)
        }

    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun albumIncludedSongsPlay(albumIncludeMusic: AlbumIncludeMusic) {
        mainViewModel.updateMiniPlayerUI(albumIncludeMusic)
    }
}
