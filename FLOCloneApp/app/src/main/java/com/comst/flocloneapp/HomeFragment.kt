package com.comst.flocloneapp

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.adapter.EverydayMusicAdapter
import com.comst.flocloneapp.adapter.TodayMusicAdapter
import com.comst.flocloneapp.adapter.VideoMusicAdapter
import com.comst.flocloneapp.databinding.FragmentHomeBinding
import com.comst.flocloneapp.listener.ItemTodayMusicListener
import com.comst.flocloneapp.model.EverydayMusic
import com.comst.flocloneapp.model.TodayMusic
import com.comst.flocloneapp.model.VideoMusic

class HomeFragment : Fragment(), ItemTodayMusicListener {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val todayMusicAdapter = TodayMusicAdapter(this)
    private val everydayMusicAdapter = EverydayMusicAdapter()
    private val videoMusicAdapter = VideoMusicAdapter()

    private val todayMusicList = mutableListOf<TodayMusic>()
    private val everyMusicList = mutableListOf<EverydayMusic>()
    private val videoMusicList = mutableListOf<VideoMusic>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.appbarLayout.setPadding(0,getStatusBarHeight(requireContext())/2, 0, 0)

        initView()

        return binding.root
    }

    private fun initView(){
        with(binding){

            todayMusicDummy()
            everydayMusicDummy()
            videoMusicDummy()

            todayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            todayMusicRecyclerView.adapter = todayMusicAdapter
            todayMusicAdapter.submitList(todayMusicList)

            everydayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            everydayMusicRecyclerView.adapter = everydayMusicAdapter
            everydayMusicAdapter.submitList(everyMusicList)

            videoMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            videoMusicRecyclerView.adapter = videoMusicAdapter
            videoMusicAdapter.submitList(videoMusicList)

        }
    }

    // extention.kt
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    private fun todayMusicDummy(){

        for (i in 1..6){
            val dummy = TodayMusic(i,"LILAC $i", "아이유 (IU)")
            todayMusicList.add(dummy)
        }
    }

    private fun everydayMusicDummy(){

        for (i in 1..6){
            val dummy = EverydayMusic(i,"제목 $i", "가수")
            everyMusicList.add(dummy)
        }
    }

    private fun videoMusicDummy(){
        for (i in 1..6){
            val dummy = VideoMusic(i,"제목 $i", "가수")
            videoMusicList.add(dummy)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun goAlbumFragment(position: Int) {

        val bundle = Bundle().apply {
            putString("albumName", todayMusicList[position].musicName)
            putString("artistName", todayMusicList[position].artist)
        }

        findNavController().navigate(R.id.albumFragment,bundle)

    }
}

class TodayMusicAdapterDecoration : RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.right = 30
    }
}