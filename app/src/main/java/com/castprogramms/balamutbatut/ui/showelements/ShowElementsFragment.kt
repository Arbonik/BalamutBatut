package com.castprogramms.balamutbatut.ui.showelements

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentAddElementsBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.ui.addelements.AddElementsAdapter
import com.castprogramms.balamutbatut.ui.addelements.AddElementsViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowElementsFragment : Fragment(R.layout.fragment_add_elements) {
    val viewModel : AddElementsViewModel by viewModel()
    var idStudent = ""
    var titleGroup = ""
    var colorGroup = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idStudent = requireArguments().getString("id", "")
        titleGroup = requireArguments().getString("title", "")
        colorGroup = requireArguments().getInt("color", 0)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAddElementsBinding.bind(view)
        val adapter = ShowElementsAdapter(colorGroup)
        binding.recycler.adapter = adapter
        viewModel.getAllElementsOnThisTitle(titleGroup).observe(viewLifecycleOwner,{
            when(it){
                is Resource.Error -> {
                    Snackbar.make(view, it.data.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null){
                        adapter.elements = it.data
                    }
                }
            }
        })

        adapter.liveDataSelectedElements.observe(viewLifecycleOwner, {
            Log.e("it", it.toString())
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