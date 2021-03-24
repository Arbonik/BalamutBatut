package com.castprogramms.balamutbatut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EditProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val acceptEditButton : MaterialButton = view.findViewById(R.id.accept_edit)
        val userName : TextInputEditText = view.findViewById(R.id.name_user)
        val userLastName : TextInputEditText = view.findViewById(R.id.last_name_user)

        acceptEditButton.setOnClickListener {

        }
        return view
    }
}