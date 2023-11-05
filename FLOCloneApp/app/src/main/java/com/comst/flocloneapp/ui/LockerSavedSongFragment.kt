package com.comst.flocloneapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.comst.flocloneapp.R
import com.comst.flocloneapp.databinding.FragmentLockerSavedSongBinding
import com.comst.flocloneapp.listener.SavedMusicListener
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
        savedMusicList.add(LockerSavedMusic(1, R.drawable.img_album_exp2, "LILAC", "아이유 (IU)", false))
        savedMusicList.add(LockerSavedMusic(2, R.drawable.img_album_exp3, "Next Level", "aespa",false))
        savedMusicList.add(LockerSavedMusic(3, R.drawable.img_album_exp5, "뿜뿜", "모모랜드",false))
        savedMusicList.add(LockerSavedMusic(4, R.drawable.img_album_exp6, "Weekend", "태연",false))

        savedMusicList.add(LockerSavedMusic(5, R.drawable.img_album_exp2, "LILAC", "아이유 (IU)",false))
        savedMusicList.add(LockerSavedMusic(6, R.drawable.img_album_exp3, "Next Level", "aespa",false))
        savedMusicList.add(LockerSavedMusic(7, R.drawable.img_album_exp5, "뿜뿜", "모모랜드",false))
        savedMusicList.add(LockerSavedMusic(8, R.drawable.img_album_exp6, "Weekend", "태연",false))

        savedMusicList.add(LockerSavedMusic(9, R.drawable.img_album_exp2, "LILAC", "아이유 (IU)",false))
        savedMusicList.add(LockerSavedMusic(10, R.drawable.img_album_exp3, "Next Level", "aespa",false))
        savedMusicList.add(LockerSavedMusic(11, R.drawable.img_album_exp5, "뿜뿜", "모모랜드",false))
        savedMusicList.add(LockerSavedMusic(12, R.drawable.img_album_exp6, "Weekend", "태연",false))

        savedMusicList.add(LockerSavedMusic(13, R.drawable.img_album_exp2, "LILAC", "아이유 (IU)",false))
        savedMusicList.add(LockerSavedMusic(14, R.drawable.img_album_exp3, "Next Level", "aespa",false))
        savedMusicList.add(LockerSavedMusic(15, R.drawable.img_album_exp5, "뿜뿜", "모모랜드",false))
        savedMusicList.add(LockerSavedMusic(16, R.drawable.img_album_exp6, "Weekend", "태연",false))

        savedMusicList.add(LockerSavedMusic(17, R.drawable.img_album_exp2, "LILAC", "아이유 (IU)",false))
        savedMusicList.add(LockerSavedMusic(18, R.drawable.img_album_exp3, "Next Level", "aespa",false))
        savedMusicList.add(LockerSavedMusic(19, R.drawable.img_album_exp5, "뿜뿜", "모모랜드",false))
        savedMusicList.add(LockerSavedMusic(20, R.drawable.img_album_exp6, "Weekend", "태연",false))

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