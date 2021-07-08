package com.castprogramms.balamutbatut.ui.rating.rating_student

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentRatingStudentBinding
import com.castprogramms.balamutbatut.ui.rating.RatingViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RatingStudentFragment: Fragment(R.layout.fragment_rating_student) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentRatingStudentBinding.bind(view)
        binding.viewPager2.adapter = RatingViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutProfile, binding.viewPager2){ tab: TabLayout.Tab, i: Int ->
            when(i){
                0 -> tab.text = "Общий рейтинг"
                1 -> tab.text = "Рейтинг группы"
            }
            binding.viewPager2.currentItem = i
        }.attach()
        binding.viewPager2.currentItem = 0
    }
}