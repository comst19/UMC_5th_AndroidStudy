package com.comst.flocloneapp.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.comst.flocloneapp.adapter.EverydayMusicAdapter
import com.comst.flocloneapp.adapter.HomeBannerViewPagerAdapter
import com.comst.flocloneapp.adapter.TodayMusicAdapter
import com.comst.flocloneapp.adapter.VideoMusicAdapter
import com.comst.flocloneapp.databinding.FragmentHomeBinding
import com.comst.flocloneapp.listener.ItemTodayMusicListener
import com.comst.flocloneapp.model.EverydayMusic
import com.comst.flocloneapp.model.HomeBanner
import com.comst.flocloneapp.model.TodayMusic
import com.comst.flocloneapp.model.VideoMusic
import com.comst.flocloneapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), ItemTodayMusicListener {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val todayMusicAdapter = TodayMusicAdapter(this)
    private val everydayMusicAdapter = EverydayMusicAdapter()
    private val videoMusicAdapter = VideoMusicAdapter()
    private val bannerAdapter = HomeBannerViewPagerAdapter()

    private val todayMusicList = mutableListOf<TodayMusic>()
    private val everyMusicList = mutableListOf<EverydayMusic>()
    private val videoMusicList = mutableListOf<VideoMusic>()
    private val homeBannerList = mutableListOf<HomeBanner>()



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

            todayMusicDummy()
            everydayMusicDummy()
            videoMusicDummy()
            homeBannerDummy()

            todayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            todayMusicRecyclerView.adapter = todayMusicAdapter
            todayMusicAdapter.submitList(todayMusicList)

            everydayMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            everydayMusicRecyclerView.adapter = everydayMusicAdapter
            everydayMusicAdapter.submitList(everyMusicList)

            videoMusicRecyclerView.addItemDecoration(TodayMusicAdapterDecoration())
            videoMusicRecyclerView.adapter = videoMusicAdapter
            videoMusicAdapter.submitList(videoMusicList)

            binding.homeBannerViewPager.adapter = bannerAdapter
            binding.homeBannerViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            bannerAdapter.submitList(homeBannerList)
            binding.homeBannerIndicator.setViewPager(binding.homeBannerViewPager)

            binding.homeBannerIndicator.createIndicators(6, 0)

            viewLifecycleOwner.lifecycleScope.launch {
                while (isActive){
                    delay(3000L)
                    val currentPage = binding.homeBannerViewPager.currentItem
                    val totalPages = binding.homeBannerViewPager.adapter?.itemCount ?: 0
                    val nextPage = (currentPage + 1) % totalPages
                    binding.homeBannerViewPager.setCurrentItem(nextPage, false)
                }
            }
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

    private fun homeBannerDummy(){

        val bannerTitleList = mutableListOf<String>(
            "늘어지는 오후에 어울리는 멜로우 팝",
            "기타 선율이 매력적인 팝 모음 ❤️",
            "나만 알고 싶은 쓸쓸한 맛 K-발라드",
            "어쿠스틱한 팝이 흐르는 감성",
            "선선한 가을 날씨, 그리고 코지무드",
            "잠 안오는 새벽을 채워줄 팝")
        for (i in 1..6){
            val dummy = HomeBanner(i,bannerTitleList[i-1])
            homeBannerList.add(dummy)
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