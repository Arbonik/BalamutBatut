package com.castprogramms.balamutbatut.ui.allgroupelement

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentAddElementsBinding
import com.castprogramms.balamutbatut.network.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllGroupElementFragment: Fragment(R.layout.fragment_add_elements) {
    val viewModel: AllGroupElementViewModel by viewModel()
    var titleGroup = ""
    var colorGroup = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleGroup = requireArguments().getString("title", "")
        colorGroup = requireArguments().getInt("color", 0)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAddElementsBinding.bind(view)
        val adapter = AllGroupElementAdapter(colorGroup, titleGroup)
        binding.recycler.adapter = adapter
        viewModel.getAllElementsOnThisTitle(titleGroup).observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null)
                        adapter.elements = it.data
                }
            }
        })

    }
}