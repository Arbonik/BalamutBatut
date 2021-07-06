package com.castprogramms.balamutbatut.ui.addgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.tools.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.databinding.FragmentAddGroupBinding
import com.castprogramms.balamutbatut.tools.User
import org.koin.android.ext.android.inject

class AddGroupFragment: Fragment() {
    val repository : Repository by inject()
    val listCheck = mutableListOf<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddGroupBinding.inflate(inflater)
        binding.addGroup.setOnClickListener {
            listCheck.clear()
            isValid(binding.nameGroup)
            isValid(binding.descGroup)
            if (!listCheck.contains(false)){
                repository.addGroup(
                    Group(binding.nameGroup.text.toString(),
                        binding.descGroup.text.toString(),
                        User.id)
                )
                findNavController().navigate(R.id.action_addGroupFragment_to_group_Fragment)
            }
        }
        return binding.root
    }
    private fun isValid(plainText : EditText) {
        if (plainText.text.isNullOrEmpty() && plainText.text.isNullOrBlank()) {
            plainText.error = ""
            listCheck.add(false)
        }
    }
}