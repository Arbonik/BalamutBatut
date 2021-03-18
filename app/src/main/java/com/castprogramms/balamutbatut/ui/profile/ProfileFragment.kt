package com.castprogramms.balamutbatut.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
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
        val view = inflater.inflate(R.layout.profile_fragment_student, container, false)
        val tabs: TabLayout = view.findViewById(R.id.tab_layout)
        val viewPager2: ViewPager2 = view.findViewById(R.id.view_pager2)
        viewPager2.adapter = ViewPager2FragmentAdapter(this)
        TabLayoutMediator(tabs, viewPager2) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                0 -> tab.text = "Навыки"
                1 -> tab.text = "Достижения"
            }
            viewPager2.currentItem = i
        }.attach()
        viewPager2.currentItem = 0
        User.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val binging = ProfileBinding.bind(view.findViewById(R.id.profile_student))
                binging.person = it
                if (User.img != "null")
                    Glide.with(this)
                        .load(User.img)
                        .into(binging.icon)
            }
        })
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

    }

}