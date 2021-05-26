package com.castprogramms.balamutbatut.ui.allElements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.AllElementListFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllElementListFragment : Fragment() {
    val viewModel: AllElementListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.all_element_list_fragment, container, false)
        val binding = AllElementListFragmentBinding.bind(view)

        viewModel.mutableLiveElements.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                generateAdapter(it).observe(viewLifecycleOwner) { it1 ->
                    binding.recyclerAllElementsList.adapter = AdapterAllElements(it1)
                }
            }
        })

        return view
    }
    fun generateAdapter(map: MutableList<Int>): MutableLiveData<MutableList<Int>> {
        val maps = mutableMapOf<String, List<Int>>()
        val mutableMap = MutableLiveData(map)
        viewModel.getAllElement(maps).observe(viewLifecycleOwner) { it1 ->
            maps.putAll(it1 as Map<out String, List<Int>>)
            mutableMap.postValue(mutableMap.value)
        }
        return mutableMap
    }
}