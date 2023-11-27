package com.comst.flocloneapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.network.FLOSongService
import com.comst.flocloneapp.databinding.FragmentLockerLikeSongBinding
import com.comst.flocloneapp.databinding.FragmentLockerSavedAlbumBinding
import com.comst.flocloneapp.databinding.FragmentLookBinding
import com.comst.flocloneapp.model.local.SongEntity
import com.comst.flocloneapp.model.network.FloChartResult
import com.comst.flocloneapp.ui.adapter.LockerLikeSongAdapter
import com.comst.flocloneapp.ui.adapter.LookFragmentFloChartAdapter
import com.comst.flocloneapp.ui.response.LookView
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel


class LookFragment : Fragment(), LookView {

    private var _binding : FragmentLookBinding? = null
    private val binding get() = _binding!!

    private val floChartSongAdapter = LookFragmentFloChartAdapter()

    override fun onStart() {
        super.onStart()
        getSongs()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLookBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    fun initView(){

        with(binding){
            lookFloChartRv.adapter = floChartSongAdapter
        }
    }

    private fun getSongs() {
        val songService = FLOSongService()
        songService.setLookView(this)

        songService.getSongs()

    }

    private fun initRecyclerView(result: FloChartResult) {
        binding.lookFloChartRv.adapter = floChartSongAdapter
        floChartSongAdapter.submitList(result.songs)
    }

    override fun onGetSongLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        binding.lookLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}