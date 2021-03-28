package com.castprogramms.balamutbatut.tools

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.castprogramms.balamutbatut.ui.node.ChooseNodeViewFragment

class NodeViewPager2Adapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            return ChooseNodeViewFragment()
        else
            return ChooseNodeViewFragment()
    }
}