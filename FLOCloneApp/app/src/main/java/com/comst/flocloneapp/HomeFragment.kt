package com.comst.flocloneapp

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.comst.flocloneapp.adapter.EverydayMusicAdapter
import com.comst.flocloneapp.adapter.TodayMusicAdapter
import com.comst.flocloneapp.adapter.VideoMusicAdapter
import com.comst.flocloneapp.databinding.FragmentHomeBinding
import com.comst.flocloneapp.model.EverydayMusic
import com.comst.flocloneapp.model.TodayMusic
import com.comst.flocloneapp.model.VideoMusic

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val todayMusicAdapter = TodayMusicAdapter()
    private val everydayMusicAdapter = EverydayMusicAdapter()
    private val videoMusicAdapter = VideoMusicAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){
        with(binding){
            binding.toolbar.setPadding(0,getStatusBarHeight(requireContext()), 0, 0)

            todayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            todayMusicRecyclerView.adapter = todayMusicAdapter
            todayMusicAdapter.submitList(todayMusicDummy())

            everydayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            everydayMusicRecyclerView.adapter = everydayMusicAdapter
            everydayMusicAdapter.submitList(everydayMusicDummy())

            videoMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            videoMusicRecyclerView.adapter = videoMusicAdapter
            videoMusicAdapter.submitList(videoMusicDummy())

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

    private fun todayMusicDummy() : List<TodayMusic>{

        val dummyList = mutableListOf<TodayMusic>()

        for (i in 1..6){
            val dummy = TodayMusic(i,"LILAC $i", "아이유 (IU)")
            dummyList.add(dummy)
        }
        return dummyList
    }

    private fun everydayMusicDummy() : List<EverydayMusic>{

        val dummyList = mutableListOf<EverydayMusic>()

        for (i in 1..6){
            val dummy = EverydayMusic(i,"제목 $i", "가수")
            dummyList.add(dummy)
        }
        return dummyList
    }

    private fun videoMusicDummy() : List<VideoMusic>{

        val dummyList = mutableListOf<VideoMusic>()

        for (i in 1..6){
            val dummy = VideoMusic(i,"제목 $i", "가수")
            dummyList.add(dummy)
        }
        return dummyList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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