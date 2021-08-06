package com.castprogramms.balamutbatut.ui.groupelements

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentGroupElementsBinding
import com.castprogramms.balamutbatut.network.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupElementsFragment: Fragment(R.layout.fragment_group_elements) {
    var idStudent = ""
    val viewModel : GroupElementsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idStudent = requireArguments().getString("id", "")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentGroupElementsBinding.bind(view)
        requireActivity().title = "Списки элементов"
        val adapter = GroupElementsAdapter(idStudent)
        binding.recyclerElements.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerElements.adapter = adapter
        viewModel.getStudent(idStudent)
        viewModel.lifeData.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {
                    binding.recyclerElements.hideShimmer()
                    Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.recyclerElements.showShimmer()
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        adapter.elementsTitle = it.data
                        binding.recyclerElements.hideShimmer()
                    }
                }
            }
        })
    }
}