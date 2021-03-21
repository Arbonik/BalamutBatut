package com.castprogramms.balamutbatut.ui.node

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.NodeViewPager2Adapter
import com.castprogramms.balamutbatut.tools.ViewPager2FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NodeViewFragment : Fragment() {

    private lateinit var viewModel: NodeViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.node_view_fragment, container, false)
        val tab : TabLayout = view.findViewById(R.id.node_tab_layout)
        val viewPager : ViewPager2 = view.findViewById(R.id.nodePager)
        viewPager.adapter = NodeViewPager2Adapter(this)
        TabLayoutMediator(tab, viewPager){ tab : TabLayout.Tab, i: Int ->
            when(i) {
                0 -> tab.text = "Вариант 1"
                1 -> tab.text = "Вариант 2"
            }
            viewPager.currentItem = i
        }.attach()
        viewPager.currentItem = 0
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NodeViewViewModel::class.java)

    }
}