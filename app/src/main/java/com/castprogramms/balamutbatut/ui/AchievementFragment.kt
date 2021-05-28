package com.castprogramms.balamutbatut.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentAchievementBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.changeprogram.ChangeElementsViewModel
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import org.koin.androidx.viewmodel.ext.android.viewModel

class AchievementFragment(val id: String) : FragmentWithElement(R.layout.fragment_achievement) {
    val viewModel : ChangeElementsViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAchievementBinding.bind(view)
        val adapterList = IHopeThisAdapterCanWork(true)
        binding.recyclerAchiStudent.adapter = adapterList
        binding.recyclerAchiStudent.layoutManager = LinearLayoutManager(requireContext())
        getTrueOrder()
        User.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null){
                generateAdapter(it.element)
            }
        })
        mutableLiveDataOrder.observe(viewLifecycleOwner, {
            if (it is Resource.Success && it.data != null)
                adapterList.filters = it.data
        })
        mutableLiveData.observe(viewLifecycleOwner, {
            adapterList.setElement(it)
            binding.recyclerAchiStudent.adapter = adapterList
        })
    }
}