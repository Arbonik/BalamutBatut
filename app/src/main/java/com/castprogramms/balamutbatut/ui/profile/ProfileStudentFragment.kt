package com.castprogramms.balamutbatut.ui.profile

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentProfileStudentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.User
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileStudentFragment : Fragment(R.layout.fragment_profile_student) {
    val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentProfileStudentBinding.bind(view)
        this.setHasOptionsMenu(true)

        User.mutableLiveDataStudent.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.fullName.text = it.getFullName()
                Glide.with(this)
                    .load(it.img)
                    .addListener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressImageProfile.visibility = View.GONE
                            binding.imageUser.setImageResource(R.drawable.male_user)

                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressImageProfile.visibility = View.GONE
                            binding.imageUser.setImageDrawable(resource)

                            return true
                        }
                    }).into(binding.imageUser)
                binding.quantityBatutCoin.text = it.batutcoin.toString()
                binding.quantityElements.text = it.getQuantityElements()
            }
        })

        val adapter = HelpAdapter()
        binding.recyclerHelp.adapter = adapter
        binding.recyclerHelp.layoutManager = LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
        binding.recyclerHelp.setHasFixedSize(true)

        viewModel.getPlaceStudentInRating(User.id)
        viewModel.lifeDataPlace.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error ->
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null){
                        binding.ratingPlace.text = "Место в рейтинге: " + it.data
                    }
                }
            }
        })

        val animImg: Animation = AlphaAnimation(0.3f, 1.0f)
        animImg.duration = 3100

        val countDownTimer = object : CountDownTimer(2900, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.imageUser.startAnimation(animImg)
            }

            override fun onFinish() {
                binding.butTextEdit.visibility = View.GONE
            }
        }

        binding.imageUser.setOnClickListener {
            countDownTimer.start()
            binding.butTextEdit.visibility = View.VISIBLE
        }

        binding.butTextEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.scanContainer.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_qrCodeFragment)
        }

        binding.inviteContainer.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_qrCodeFragment)
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
        }
        return true
    }
}