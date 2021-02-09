package com.castprogramms.balamutbatut.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.ViewPager2FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        val nameUser: TextView = view.findViewById(R.id.full_name)
        val dateUser: TextView = view.findViewById(R.id.about)
        val sexUser: TextView = view.findViewById(R.id.sex)
        val tabs : TabLayout = view.findViewById(R.id.tab_layout)
        val viewPager2 : ViewPager2 = view.findViewById(R.id.view_pager2)
        viewPager2.adapter = ViewPager2FragmentAdapter(this)
        TabLayoutMediator(tabs, viewPager2){ tab: TabLayout.Tab, i: Int ->
            when(i){
                0 -> tab.text = "Навыки"
                1 -> tab.text = "Достижения"
            }
            viewPager2.currentItem = i
        }.attach()
        viewPager2.currentItem = 0
        User.mutableLiveDataStudent.observe(viewLifecycleOwner, {
            nameUser.text = it?.name
        })
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

    }

}