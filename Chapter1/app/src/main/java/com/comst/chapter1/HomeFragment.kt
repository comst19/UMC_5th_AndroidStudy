package com.comst.chapter1

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comst.chapter1.databinding.FragmentHomeBinding
import com.comst.chapter1.model.TodayMusic
import com.comst.chapter1.model.VideoMusic


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val todayMusicAdapter = TodayMusicAdapter()
    private val everydayMusicAdapter = TodayMusicAdapter()
    private val videoMusicAdapter = VideoMusicAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }

    private fun initView(){
        with(binding){

            todayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            todayMusicRecyclerView.adapter = todayMusicAdapter
            todayMusicAdapter.submitList(todayMusicDummy())

            everydayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            everydayMusicRecyclerView.adapter = everydayMusicAdapter
            everydayMusicAdapter.submitList(todayMusicDummy())

            videoMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            videoMusicRecyclerView.adapter = videoMusicAdapter
            videoMusicAdapter.submitList(videoMusicDummy())

        }
    }

    private fun todayMusicDummy() : List<TodayMusic>{

        val dummyList = mutableListOf<TodayMusic>()

        val dummy1 = TodayMusic(1)
        val dummy2 = TodayMusic(2)
        val dummy3 = TodayMusic(3)
        val dummy4 = TodayMusic(4)
        val dummy5 = TodayMusic(5)
        val dummy6 = TodayMusic(6)

        dummyList.add(dummy1)
        dummyList.add(dummy2)
        dummyList.add(dummy3)
        dummyList.add(dummy4)
        dummyList.add(dummy5)
        dummyList.add(dummy6)

        return dummyList
    }

    private fun videoMusicDummy() : List<VideoMusic>{

        val dummyList = mutableListOf<VideoMusic>()

        val dummy1 = VideoMusic(1)
        val dummy2 = VideoMusic(2)
        val dummy3 = VideoMusic(3)
        val dummy4 = VideoMusic(4)
        val dummy5 = VideoMusic(5)
        val dummy6 = VideoMusic(6)

        dummyList.add(dummy1)
        dummyList.add(dummy2)
        dummyList.add(dummy3)
        dummyList.add(dummy4)
        dummyList.add(dummy5)
        dummyList.add(dummy6)

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