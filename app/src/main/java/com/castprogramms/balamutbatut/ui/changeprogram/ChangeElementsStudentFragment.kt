package com.castprogramms.balamutbatut.ui.changeprogram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentChangeProgramBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import com.castprogramms.balamutbatut.users.Student
import org.koin.android.viewmodel.ext.android.viewModel

class ChangeElementsStudentFragment: FragmentWithElement(R.layout.fragment_change_program) {
    var student : Student? = null
    var id = ""
    var idElements = arrayOf<String>()
    val mutableLiveDataStudent = MutableLiveData(student)
    val changeElementsViewModel: ChangeElementsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            id = arguments?.getString("id").toString()
            idElements = arguments?.getStringArray("idElements") as Array<String>
        }
        if (id != "" && id != "null"){
            repository.getStudent(id).addSnapshotListener { value, error ->
                if (value != null){
                    student = value.toObject(Student::class.java)
                    mutableLiveDataStudent.postValue(student)
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentChangeProgramBinding.bind(view)
        val adapter = IHopeThisAdapterCanWork()
        binding.recyclerElements.adapter = adapter
        binding.recyclerElements.layoutManager = LinearLayoutManager(requireContext())
        changeElementsViewModel.getTrueOrder().observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    adapter.filters = it.data!!
                }
            }
        })
        mutableLiveDataStudent.observe(viewLifecycleOwner, {
            if (it != null){
                generateAdapter(it.element)
            }
        })
        mutableLiveData.observe(viewLifecycleOwner) {
            adapter.setElement(it)
        }

        binding.checkFab.setOnClickListener {
            repository.updateElementsStudent(adapter.checkedElements.toMap(), id)
            val bundle = Bundle()
            bundle.putString("id", id)
            findNavController()
                .navigate(R.id.action_changeProgramFragment_to_infoStudentFragment2, bundle)
        }
    }
    override fun generateAdapter(map: Map<String, List<Int>>) {
        val maps = mutableMapOf<String, List<Element>>()
        repository.getAllElements(map).observe(viewLifecycleOwner) { it1 ->
            maps.putAll(it1 as Map<out String, List<Element>>)
            mutableLiveData.postValue(maps)
        }
    }
}