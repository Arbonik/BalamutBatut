package com.castprogramms.balamutbatut.ui.profile.profile_trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.User

class ProfileTrainerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment_trainer, container, false)
        val firstNameTextView : TextView = view.findViewById(R.id.first_name_trainer)
        val secondNameTextView : TextView = view.findViewById(R.id.second_name_trainer)
        val sexTextView : TextView = view.findViewById(R.id.sex_trainer)
        val groupTextView : TextView = view.findViewById(R.id.group_trainer)
        val bitrhdayTextView : TextView = view.findViewById(R.id.bitrhday_trainer)
        User.mutableLiveDataTrainer.observe(viewLifecycleOwner, Observer {
            if (it != null){
                firstNameTextView.text = it.first_name
                secondNameTextView.text = it.second_name
                sexTextView.text = it.sex
                groupTextView.text = it.groupID
                bitrhdayTextView.text = it.date
            }
        })


        return view
    }
}