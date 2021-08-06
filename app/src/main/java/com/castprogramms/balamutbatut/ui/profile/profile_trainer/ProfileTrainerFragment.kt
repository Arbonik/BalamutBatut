package com.castprogramms.balamutbatut.ui.profile.profile_trainer

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentProfileTrainerBinding
import com.castprogramms.balamutbatut.tools.ActionsWithCoins
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User

class ProfileTrainerFragment: Fragment(R.layout.fragment_profile_trainer) {
    private val CAMERA_PERMISSION_CODE_FOR_SCAN_BUTTON = 0

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_CODE_FOR_SCAN_BUTTON) {
            if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                toScanner()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val binding = FragmentProfileTrainerBinding.bind(view)
        requireActivity().setTitle(R.string.item_profile)

        User.mutableLiveDataTrainer.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.fullName.text = it.getFullName()
                Log.e("date", binding.imageUser.measuredHeight.toString())
                Glide.with(this)
                    .load(it.img)
                    .addListener(object : RequestListener<Drawable>{
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
                    })
                    .into(binding.imageUser)
            }
        })
        binding.scanContainer.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_CODE_FOR_SCAN_BUTTON
                    )
                }
            } else {
                toScanner()
            }
        }

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
            findNavController().navigate(R.id.action_profile_Fragment_to_editProfileFragment2)
        }

    }

    private fun toScanner() {
        Log.e("test", findNavController().graph.toString())
        findNavController()
            .navigate(R.id.action_profile_Fragment_to_qrCodeScannerFragment2,
                Bundle().apply{
                    putString("action", ActionsWithCoins.PAY.desc)
                    putString("type", "trainer_scan")
                })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item__bar_settings -> {
                findNavController().navigate(R.id.action_profile_Fragment_to_settingsFragment2)
            }
            R.id.item__bar_all_element -> {
                if (User.typeUser == TypesUser.TRAINER)
                    findNavController().navigate(R.id.action_profile_Fragment_to_allElementListFragment2)
            }
        }
        return true
    }
}