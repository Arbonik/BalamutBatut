package com.castprogramms.balamutbatut.ui.allElements

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.AllElementListFragmentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.ui.groupelements.GroupElementsViewModel
import com.google.android.material.snackbar.Snackbar
import com.todkars.shimmer.ShimmerAdapter
import com.todkars.shimmer.ShimmerRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllElementListFragment : Fragment(R.layout.all_element_list_fragment) {
    val viewModel: GroupElementsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().setTitle(R.string.item_all_elements)
        val binding = AllElementListFragmentBinding.bind(view)
        val adapter = AllElementsAdapter()
        binding.recyclerAllElementsList.shimmerLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAllElementsList.adapter = adapter
        viewModel.getSortedStudentTitleElementsWithColor(mapOf()).observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.recyclerAllElementsList.showShimmer()
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        binding.recyclerAllElementsList.hideShimmer()
                        adapter.elementsTitle = it.data
                    }
                }
            }
        })
    }
}