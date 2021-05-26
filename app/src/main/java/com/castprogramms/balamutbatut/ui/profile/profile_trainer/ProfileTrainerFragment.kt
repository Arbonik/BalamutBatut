package com.castprogramms.balamutbatut.ui.profile.profile_trainer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.infostudent.InfoStudentViewModel
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileTrainerFragment: Fragment() {
    val viewModel : InfoStudentViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.profile_fragment_trainer, container, false)
        val binding = ProfileBinding.bind(view.findViewById(R.id.profile_trainer))
        binding.batutContainer.visibility = View.GONE
        User.mutableLiveDataTrainer.observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.person = it
                viewModel.getGroupName(it.groupID)
                    .addSnapshotListener { value, error ->
                        if (value != null)
                            binding.groupID.text = value.getString("name")
                    }
                if (it.img != "")
                    Glide.with(this)
                        .load(it.img)
                        .into(binding.icon)
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
            findNavController().navigate(R.id.action_profile_Fragment_to_editProfileFragment2)
        }

        return view
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
        }
        return true
    }
}