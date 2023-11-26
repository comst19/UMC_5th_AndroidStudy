package com.comst.flocloneapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.comst.flocloneapp.R
import com.comst.flocloneapp.data.db.SongDatabase
import com.comst.flocloneapp.ui.adapter.AlbumFragmentViewPagerAdapter
import com.comst.flocloneapp.databinding.FragmentAlbumBinding
import com.comst.flocloneapp.model.local.LikeEntity
import com.comst.flocloneapp.util.ToastLikeOnOff
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumFragment : Fragment(){

    private var _binding : FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    lateinit var songDB : SongDatabase
    private val miniPlayerViewModel : MiniPlayerViewModel by activityViewModels()

    private var userIdx : Int = 0
    private var albumId : Int = 0
    private var isLiked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        val spf = requireContext().getSharedPreferences("song", AppCompatActivity.MODE_PRIVATE)
        userIdx = spf.getInt("currentUserIdx",0)

        //Toast.makeText(requireContext(), userIdx.toString(), Toast.LENGTH_SHORT).show()

        initView()
        setupToolbar()
        return binding.root
    }

    private fun initView(){

        //val albumName = arguments?.getString("albumName")
        //val artistName = arguments?.getString("artistName")
        albumId = miniPlayerViewModel.albumId.value!!

        CoroutineScope(Dispatchers.IO).launch{
            val album = songDB.AlbumDao().getAlbum(albumId)

            withContext(Dispatchers.Main){
                binding.albumTitle.text = album.title
                binding.albumArtist.text = album.singer
                binding.albumImage.setImageResource(album.coverImg!!)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val isLike = songDB.AlbumDao().isLikedAlbum(userIdx, albumId)
            isLiked = isLike != null
            withContext(Dispatchers.Main) {
                activity?.invalidateOptionsMenu()
            }
        }

        with(binding){
            appbarLayout.setPadding(0,getStatusBarHeight(requireContext()), 0, 0)


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

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val likeItem = menu.findItem(R.id.likeIcon)
        if (isLiked) {
            likeItem.setIcon(R.drawable.ic_my_like_on)
        } else {
            likeItem.setIcon(R.drawable.ic_my_like_off)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.likeIcon -> {

                isLiked = !isLiked

                if (isLiked) {
                    item.setIcon(R.drawable.ic_my_like_on)
                    ToastLikeOnOff.createToast(requireContext(), "좋아요 한 앨범에 담겼습니다.")?.show()
                    lifecycleScope.launch(Dispatchers.IO) {
                        songDB.AlbumDao().likeAlbum(LikeEntity(userIdx, albumId))
                    }
                }else{
                    item.setIcon(R.drawable.ic_my_like_off)
                    ToastLikeOnOff.createToast(requireContext(), "좋아요 한 앨범이 취소되었습니다.")?.show()
                    lifecycleScope.launch(Dispatchers.IO) {
                        songDB.AlbumDao().disLikeAlbum(userIdx, albumId)
                    }
                }


                true
            }
            else -> false
        }
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