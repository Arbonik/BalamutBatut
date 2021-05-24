package com.castprogramms.balamutbatut.tools

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.castprogramms.balamutbatut.ui.AchievementFragment

class ViewPager2FragmentAdapter(fragment: Fragment, val id: String): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int) = AchievementFragment(id)
}