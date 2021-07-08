package com.castprogramms.balamutbatut.ui.rating

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.castprogramms.balamutbatut.tools.RatingType

class RatingViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
                RatingFragment(RatingType.All)
            else
                RatingFragment(RatingType.Group)
    }
}