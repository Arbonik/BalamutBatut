package com.castprogramms.balamutbatut.ui.profile.profile_trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.tools.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileTrainerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment_trainer, container, false)
        User.mutableLiveDataTrainer.observe(viewLifecycleOwner, Observer {
            if (it != null){
                val binging = ProfileBinding.bind(view.findViewById(R.id.profile_trainer))
                binging.person = it
                if (User.img != "null")
                Glide.with(this)
                    .load(User.img)
                    .into(binging.icon)
            }
        })
        return view
    }
}