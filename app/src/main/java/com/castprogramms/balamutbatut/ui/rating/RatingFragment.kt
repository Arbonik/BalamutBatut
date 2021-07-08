package com.castprogramms.balamutbatut.ui.rating

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentRatingBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.RatingType
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Student
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingFragment(private val groupType: RatingType = RatingType.All) : Fragment(R.layout.fragment_rating) {
    val viewModel: RatingViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentRatingBinding.bind(view)
        initAdapter(binding)
    }

    private fun initAdapter(binding: FragmentRatingBinding) {
        val ratingAdapter = RatingAdapter { viewModel.getRang(it) }
        binding.recyclerRating.adapter = ratingAdapter
        var livedata = MutableLiveData<Resource<List<Pair<String, Student>>>>()
        when (groupType) {
            RatingType.All -> {
                livedata = viewModel.getAllStudent()
            }
            RatingType.Group -> {
                livedata = viewModel.getStudentGroups(User.student?.groupID.toString())
                binding.titleRatingGroup.root.visibility = View.VISIBLE
                if (User.student != null) {
                    viewModel.getGroup(User.student!!.groupID).observe(viewLifecycleOwner, {
                        if (it != null) {
                            when (it) {
                                is Resource.Error -> {
                                }
                                is Resource.Loading -> {
                                }
                                is Resource.Success -> {
                                    if (it.data != null) {
                                        binding.titleRatingGroup.nameTrainerGroup.text
                                        binding.titleRatingGroup.nameGroup.text = it.data.name
                                        binding.titleRatingGroup.descGroup.text =
                                            it.data.description
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
        livedata.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {
                    binding.progressBarRating.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {
                    binding.progressBarRating.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBarRating.progressBar.visibility = View.GONE
                    if (it.data != null)
                        ratingAdapter.students = it.data.toMutableList()
                }
            }
        })
    }
}