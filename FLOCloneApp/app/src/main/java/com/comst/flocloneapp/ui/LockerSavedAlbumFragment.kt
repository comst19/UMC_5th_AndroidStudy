package com.comst.flocloneapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.databinding.FragmentLockerSavedAlbumBinding
import com.comst.flocloneapp.model.AlbumEntity
import com.comst.flocloneapp.ui.adapter.LockerSavedAlbumAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LockerSavedAlbumFragment : Fragment() {

    private var _binding : FragmentLockerSavedAlbumBinding? = null
    private val binding get() = _binding!!

    private val savedSongAdapter = LockerSavedAlbumAdapter()

    lateinit var songDB : SongDatabase

    private var likeSongList = mutableListOf<AlbumEntity>()
    private var userIdx : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLockerSavedAlbumBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        val spf = requireContext().getSharedPreferences("song", AppCompatActivity.MODE_PRIVATE)
        userIdx = spf.getInt("currentUserIdx",0)
        initView()

        savedAlbums()

        return binding.root
    }

    private fun initView(){



        with(binding){

            lockerSavedSongRecyclerView.adapter = savedSongAdapter
        }
    }

    private fun savedAlbums(){

        CoroutineScope(Dispatchers.IO).launch {
            likeSongList = songDB.AlbumDao().getLikedAlbums(userIdx).toMutableList()

            withContext(Dispatchers.Main){
                savedSongAdapter.submitList(likeSongList)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}