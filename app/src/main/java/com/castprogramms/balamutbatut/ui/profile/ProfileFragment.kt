package com.castprogramms.balamutbatut.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.ViewPager2FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.profile_fragment_student, null)

        (requireActivity() as MainActivityStudent).hideOptionsMenu()
        val editProfile_but : ImageButton = view.findViewById(R.id.editProfile)
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
        editProfile_but.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        User.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                try {
                    val binging = ProfileBinding.bind(view.findViewById(R.id.profile_student))
                    binging.person = it
                    if (it.img != "null")
                        Glide.with(this)
                            .load(it.img)
                            .into(binging.icon)
                }catch (e: Exception){
                    Log.e("data", e.message.toString())
                }
            }
        })
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

    }
}