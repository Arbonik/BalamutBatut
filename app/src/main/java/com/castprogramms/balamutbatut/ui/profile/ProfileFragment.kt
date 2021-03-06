package com.castprogramms.balamutbatut.ui.profile

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.databinding.ProfileFragmentStudentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.ViewPager2FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.profile_fragment_student) {
    val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = ProfileFragmentStudentBinding.bind(view)
        this.setHasOptionsMenu(true)
        binding.viewPager2.adapter = ViewPager2FragmentAdapter(this, "")
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                0 -> tab.text = "Навыки"
                1 -> tab.text = "Достижения"
            }
            binding.viewPager2.currentItem = i
        }.attach()
        binding.viewPager2.currentItem = 0
        viewModel.getRang(User.id)
        binding.profileStudent.rangContainer.visibility = View.VISIBLE
        viewModel.liveDataRang.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.profileStudent.rang.text = it.data
                }
            }
        })
        User.mutableLiveDataStudent.observe(viewLifecycleOwner,  {
            if (it != null) {
                try {
                    binding.profileStudent.person = it
                    Glide.with(requireContext())
                        .load(it.img)
                        .error(R.drawable.male_user)
                        .into(binding.profileStudent.icon)
                    binding.profileStudent.groupID.text = it.groupID
                    binding.profileStudent.batutCoin.text = it.batutcoin.toString()
                } catch (e: Exception) {
                    Log.e("data", e.message.toString())
                }
            }
        })
        val animImg: Animation = AlphaAnimation(0.3f, 1.0f)
        animImg.duration = 3100

        val countDownTimer = object : CountDownTimer(2900, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.profileStudent.icon.startAnimation(animImg)
            }

            override fun onFinish() {
                binding.profileStudent.butTextEdit.visibility = View.GONE
            }
        }
        binding.profileStudent.icon.setOnClickListener {
            countDownTimer.start()
            binding.profileStudent.butTextEdit.visibility = View.VISIBLE
        }
        binding.profileStudent.butTextEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item__bar_settings -> {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
            }
            R.id.item__bar_all_element -> {
                findNavController().navigate(R.id.action_profileFragment_to_allElementListFragment)
            }
        }
        return true
    }
}