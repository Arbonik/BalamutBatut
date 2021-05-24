package com.castprogramms.balamutbatut.ui.profile

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.graph.CustomAlertDialog
import com.castprogramms.balamutbatut.graph.SetDataNodeAlertDialog
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.ViewPager2FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item__bar_settings -> {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
            }
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.profile_fragment_student, container, false)
        this.setHasOptionsMenu(true)
        val tabs: TabLayout = view.findViewById(R.id.tab_layout)
        val viewPager2: ViewPager2 = view.findViewById(R.id.view_pager2)
        viewPager2.adapter = ViewPager2FragmentAdapter(this, "")
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
                try {
                    val binging = ProfileBinding.bind(view.findViewById(R.id.profile_student))
                    binging.person = it
                    if (it.img != "null")
                        Glide.with(this)
                            .load(it.img)
                            .into(binging.icon)
                } catch (e: Exception) {
                    Log.e("data", e.message.toString())
                }
            }
        })

        val icon: CircleImageView = view.findViewById(R.id.icon)
        val edit_but_text: TextView = view.findViewById(R.id.but_text_edit)
        val animImg: Animation = AlphaAnimation(0.3f, 1.0f)
        animImg.duration = 3100

        val countDownTimer = object : CountDownTimer(2900, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                icon.startAnimation(animImg)
            }

            override fun onFinish() {
                edit_but_text.visibility = View.GONE
            }
        }
        icon.setOnClickListener {
            countDownTimer.start()
            edit_but_text.visibility = View.VISIBLE
        }
        edit_but_text.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

    }
}