package com.comst.flocloneapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.comst.flocloneapp.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

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