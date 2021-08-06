package com.castprogramms.balamutbatut.ui.addelements

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentAddElementsBinding
import com.castprogramms.balamutbatut.network.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddElementsFragment: Fragment(R.layout.fragment_add_elements) {
    private val viewModel : AddElementsViewModel by viewModel()
    private var idStudent = ""
    private var titleGroup = ""
    private var colorGroup = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idStudent = requireArguments().getString("id", "")
        titleGroup = requireArguments().getString("title", "")
        colorGroup = requireArguments().getInt("color", 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAddElementsBinding.bind(view)
        requireActivity().title = titleGroup
        val adapter = AddElementsAdapter(colorGroup)
        binding.recycler.adapter = adapter
        viewModel.getAddStudentElementsOnThisTitle(idStudent, titleGroup).observe(viewLifecycleOwner,{
            when(it){
                is Resource.Error -> {
                    binding.recycler.hideShimmer()
                    Snackbar.make(view, it.data.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.recycler.showShimmer()
                }
                is Resource.Success -> {
                    if (it.data != null){
                        adapter.elements = it.data
                        binding.recycler.hideShimmer()
                    }
                }
            }
        })

        adapter.liveDataSelectedElements.observe(viewLifecycleOwner, {
            if (it.isNotEmpty())
                binding.addElement.visibility = View.VISIBLE
            else
                binding.addElement.visibility = View.INVISIBLE
        })

        binding.addElement.setOnClickListener {
            if (!adapter.liveDataSelectedElements.value.isNullOrEmpty()) {
                viewModel.updateElementsStudent(
                    titleGroup, adapter.liveDataSelectedElements.value!!, idStudent
                )
                it.findNavController().popBackStack()
            }
        }
    }
}