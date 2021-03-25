package com.castprogramms.balamutbatut.ui.profile.profile_trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileTrainerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.profile_fragment_trainer, container, false)
        val editProfile_but : ImageButton = view.findViewById(R.id.editProfile)
        editProfile_but.setOnClickListener {
            findNavController().navigate(R.id.action_profile_Fragment_to_editProfileFragment2)
        }
        val binding = ProfileBinding.bind(view.findViewById(R.id.profile_trainer))
        User.mutableLiveDataTrainer.observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.person = it
                DataUserFirebase().getNameGroup(it.groupID)
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