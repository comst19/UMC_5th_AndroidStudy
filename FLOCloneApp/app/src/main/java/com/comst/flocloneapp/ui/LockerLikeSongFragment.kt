package com.comst.flocloneapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.databinding.BottomSheetAllSelectBinding
import com.comst.flocloneapp.databinding.FragmentLockerLikeSongBinding
import com.comst.flocloneapp.listener.SavedMusicListener
import com.comst.flocloneapp.model.local.SongEntity
import com.comst.flocloneapp.ui.adapter.LockerLikeSongAdapter
import com.comst.flocloneapp.util.MusicPlayServiceUtil
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LockerLikeSongFragment : Fragment(), SavedMusicListener {

    private var _binding : FragmentLockerLikeSongBinding? = null
    private val binding get() = _binding!!

    private var likeSongList = mutableListOf<SongEntity>()
    private val miniPlayerViewModel : MiniPlayerViewModel by activityViewModels()

    private val savedSongAdapter = LockerLikeSongAdapter(this)

    lateinit var songDB : SongDatabase

    private var allSelect = false

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

            allSelectLayout.setOnClickListener {
                allSelected()
            }
        }
    }
    private fun allSelected(){

        allSelect = !allSelect

        if (allSelect){
            binding.lockerSelectAllImgIv.setImageResource(R.drawable.btn_playlist_select_on)
            binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.flo))
            binding.lockerSelectAllTv.text = "선택해제"

            val bottomSheetBinding = BottomSheetAllSelectBinding.inflate(layoutInflater)
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(bottomSheetBinding.root)

            bottomSheetBinding.deleteLayout.setOnClickListener {
                bottomSheetDialog.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    songDB.SongDao().setAllLikesToFalse()

                    val updatedList =songDB.SongDao().getLikedSongs().toMutableList()
                    withContext(Dispatchers.Main){
                        savedSongAdapter.submitList(updatedList)
                    }
                }
            }

            bottomSheetDialog.setOnDismissListener {
                allSelect = false
                binding.lockerSelectAllImgIv.setImageResource(R.drawable.btn_playlist_select_off)
                binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryGrey))
            }

            bottomSheetDialog.show()
        }else{
            binding.lockerSelectAllImgIv.setImageResource(R.drawable.btn_playlist_select_off)
            binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryGrey))
            binding.lockerSelectAllTv.text = "전체선택"
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

        likeSongList[position].isLike = false

        CoroutineScope(Dispatchers.IO).launch {
            songDB.SongDao().update(likeSongList[position])

            val updatedList =songDB.SongDao().getLikedSongs().toMutableList()
            //likeSongList = updatedList
            withContext(Dispatchers.Main){
                savedSongAdapter.submitList(updatedList)
            }
        }
        //val updatedList = likeSongList.toMutableList()
        //updatedList.removeAt(position)
        //savedSongAdapter.submitList(likeSongList)
        //likeSongList = updatedList
    }
}