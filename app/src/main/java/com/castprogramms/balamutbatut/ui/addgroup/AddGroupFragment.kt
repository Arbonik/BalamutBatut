package com.castprogramms.balamutbatut.ui.addgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject

class AddGroupFragment: Fragment() {
    val repository : Repository by inject()
    val listCheck = mutableListOf<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_group, container, false)
        val nameGroup : TextInputEditText = view.findViewById(R.id.name_group)
        val descGroup : TextInputEditText = view.findViewById(R.id.desc_group)
        val addGroup : MaterialButton = view.findViewById(R.id.add_group)
        addGroup.setOnClickListener {
            listCheck.clear()
            if (nameGroup.text?.isNotBlank() != true && nameGroup.text?.isNotEmpty() != true){
                nameGroup.error = ""
                listCheck.add(false)
            }
            if (descGroup.text?.isNotBlank() != true && descGroup.text?.isNotEmpty() != true){
                descGroup.error = ""
                listCheck.add(false)
            }
            if (!listCheck.contains(false)){
                repository.addGroup(
                    Group(nameGroup.text.toString(),
                        descGroup.text.toString(),
                        User.id)
                )
                findNavController().navigate(R.id.action_addGroupFragment_to_group_Fragment)
            }
        }

        return view
    }
}