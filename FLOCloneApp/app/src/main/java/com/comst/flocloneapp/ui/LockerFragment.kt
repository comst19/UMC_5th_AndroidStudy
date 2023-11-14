package com.comst.flocloneapp.ui

import android.graphics.Color
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
import com.comst.flocloneapp.ui.adapter.LockerViewPagerAdapter
import com.comst.flocloneapp.databinding.FragmentLockerBinding
import com.comst.flocloneapp.viewmodel.MiniPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    private var _binding : FragmentLockerBinding? = null
    private val binding get() = _binding!!

    private var allSelected = false

    lateinit var songDB : SongDatabase
    private val miniPlayerViewModel : MiniPlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLockerBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        initView()

        return binding.root
    }

    private fun initView(){
        with(binding){

            binding.lockerSelectAllTv.setOnClickListener {
                allSelected()
            }

            binding.lockerSelectAllImgIv.setOnClickListener {
                allSelected()
            }

            val tabName = arrayOf<String>("저장한 곡", "음악파일", "♥좋아요")
            lockerContentVp.adapter = LockerViewPagerAdapter(requireActivity())

            TabLayoutMediator(lockerContentTb, lockerContentVp){ tab, position ->
                tab.text = tabName[position]
            }.attach()

            lockerContentTb.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
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

    private fun allSelected(){

        allSelected = !allSelected

        if (allSelected){
            binding.lockerSelectAllImgIv.setImageResource(R.drawable.btn_playlist_select_on)
            binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.flo))

            val bottomSheetBinding = BottomSheetAllSelectBinding.inflate(layoutInflater)
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(bottomSheetBinding.root)

            bottomSheetBinding.deleteLayout.setOnClickListener {
                bottomSheetDialog.dismiss()
                miniPlayerViewModel.clearLike.value = true
            }

            bottomSheetDialog.setOnDismissListener {
                allSelected = !allSelected
                binding.lockerSelectAllImgIv.setImageResource(R.drawable.btn_playlist_select_off)
                binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryGrey))

            }

            bottomSheetDialog.show()
        }else{
            binding.lockerSelectAllImgIv.setImageResource(R.drawable.btn_playlist_select_off)
            binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryGrey))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}