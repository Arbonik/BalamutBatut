package com.castprogramms.balamutbatut.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentRatingBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Element
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingFragment: Fragment(R.layout.fragment_rating) {
    val viewModel : RatingViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentRatingBinding.bind(view)
        initAdapter(binding)
    }

    private fun initAdapter(binding: FragmentRatingBinding) {
        val ratingAdapter = RatingAdapter { viewModel.getRang(it) }
        binding.recyclerRating.adapter = ratingAdapter
        viewModel.getAllStudent().observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {
                    binding.progressRating.visibility = View.GONE
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressRating.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressRating.visibility = View.GONE
                    if (it.data != null)
                        ratingAdapter.students = it.data.toMutableList()
                }
            }
        })
    }
}