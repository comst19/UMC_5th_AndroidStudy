package com.comst.flocloneapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.comst.flocloneapp.R
import com.comst.flocloneapp.adapter.AlbumFragmentViewPagerAdapter
import com.comst.flocloneapp.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment(){

    private var _binding : FragmentAlbumBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)

        initView()
        setupToolbar()
        return binding.root
    }

    private fun initView(){

        val albumName = arguments?.getString("albumName")
        val artistName = arguments?.getString("artistName")

        with(binding){
            appbarLayout.setPadding(0,getStatusBarHeight(requireContext()), 0, 0)
            binding.albumTitle.text = "IU 5th Album '$albumName'"
            binding.albumArtist.text = artistName



            val tabName = arrayOf<String>("수록곡", "상세정보", "영상")


            albumPager.adapter = AlbumFragmentViewPagerAdapter(requireActivity())

            TabLayoutMediator(albumTabLayout, albumPager){ tab, position ->
                tab.text = tabName[position]
            }.attach()

            albumTabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // 탭이 선택 되었을 때
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // 탭이 선택되지 않은 상태로 변경 되었을 때
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // 이미 선택된 탭이 다시 선택 되었을 때
                }

            })
        }

    }

    private fun setupToolbar() {

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        // Enable the up/back arrow on the toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}