package com.castprogramms.balamutbatut.ui.allElements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.AllElementListFragmentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllElementListFragment : FragmentWithElement(R.layout.all_element_list_fragment) {
    val viewModel: AllElementListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = AllElementListFragmentBinding.bind(view)
        val adapter =  IHopeThisAdapterCanWork(true)
        binding.recyclerAllElementsList.adapter = adapter
        generateAdapter(mapOf())
        getTrueOrder()
        mutableLiveDataOrder.observe(viewLifecycleOwner, {
            if (it is Resource.Success)
                adapter.filters = it.data!!
        })
        mutableLiveData.observe(viewLifecycleOwner, {
            adapter.setElement(it)
        })
    }
}