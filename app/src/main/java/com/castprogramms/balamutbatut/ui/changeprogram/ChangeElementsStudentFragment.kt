package com.castprogramms.balamutbatut.ui.changeprogram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.databinding.FragmentChangeProgramBinding
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.AddElementsExpandableListAdapter
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.ElementsAdapter
import com.castprogramms.balamutbatut.ui.rating.ExpandableList
import com.castprogramms.balamutbatut.ui.registr.RegistrViewModel
import com.castprogramms.balamutbatut.users.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject

class ChangeElementsStudentFragment: FragmentWithElement() {
    var student : Student? = null
    var id = ""
    var idElements = arrayOf<String>()
    val mutableLiveDataStudent = MutableLiveData(student)
    val changeElementsViewModel: ChangeElementsViewModel by viewModels()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_change_program, container, false)
        val binding = FragmentChangeProgramBinding.bind(view)
        val expandableList = AddElementsExpandableListAdapter(requireContext(), listOf(), mapOf())
        binding.recyclerElements.setAdapter(expandableList)
        mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null){
                generateAdapter(it.element).observe(viewLifecycleOwner) {
                    expandableList.setData(it.keys.toList(), it)
                    Log.e("data", it.toString())
                }
            }
        })

        binding.checkFab.setOnClickListener {
//            expandableList.checkedElements.forEach {
//                repository.updateElementsStudent(it, id)
//            }
//            val bundle = Bundle()
//            bundle.putString("id", id)
//            findNavController()
//                .navigate(R.id.action_changeProgramFragment_to_infoStudentFragment2, bundle)
            Log.e("data", expandableList.checkedElements.toString())
        }
        return view
    }
    override fun generateAdapter(map: Map<String, List<Int>>): MutableLiveData<MutableMap<String, List<Element>>> {
        val maps = mutableMapOf<String, List<Element>>()
        val mutableMap = MutableLiveData(maps)
        map.forEach {
            repository.getAllElements(mapOf(it.key to it.value)).observe(viewLifecycleOwner) { it1 ->
                maps.putAll(it1 as Map<out String, List<Element>>)
                mutableMap.postValue(maps)
            }
        }
        return mutableMap
    }

}