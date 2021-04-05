package com.castprogramms.balamutbatut.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentRatingBinding
import org.koin.android.viewmodel.ext.android.viewModel

class RatingFragment: Fragment() {
    val viewModel : RatingViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)
        val binding = FragmentRatingBinding.bind(view)
        val ratingAdapter = RatingAdapter(viewModel.getAllStudent())
        binding.recyclerRating.adapter = ratingAdapter

        return view
    }
}