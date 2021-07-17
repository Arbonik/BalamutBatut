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
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import com.castprogramms.balamutbatut.ui.groupelements.GroupElementsAdapter
import com.castprogramms.balamutbatut.ui.groupelements.GroupElementsViewModel
import com.castprogramms.balamutbatut.users.Student
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllElementListFragment : Fragment(R.layout.all_element_list_fragment) {
    val viewModel : GroupElementsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = AllElementListFragmentBinding.bind(view)
        val adapter = AllElementsAdapter()
        binding.recyclerAllElementsList.adapter = adapter
        viewModel.getSortedStudentTitleElementsWithColor(mapOf()).observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {
                    binding.progress.progressBar.visibility = View.GONE
                    Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null) {
                        binding.progress.progressBar.visibility = View.GONE
                        adapter.elementsTitle = it.data
                    }
                }
            }
        })
    }
}