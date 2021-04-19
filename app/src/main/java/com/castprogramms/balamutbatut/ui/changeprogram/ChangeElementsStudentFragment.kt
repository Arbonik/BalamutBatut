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
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import com.castprogramms.balamutbatut.users.Student

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
        val adapter = IHopeThisAdapterCanWork(viewLifecycleOwner)
        binding.recyclerElements.adapter = adapter
        binding.recyclerElements.layoutManager = LinearLayoutManager(requireContext())
        mutableLiveDataStudent.observe(viewLifecycleOwner, {
            if (it != null){
                generateAdapter(it.element).observe(viewLifecycleOwner) {
                    adapter.setElement(it)
                }
            }
        })

        binding.checkFab.setOnClickListener {
            repository.updateElementsStudent(adapter.checkedElements.toMap(), id)
            val bundle = Bundle()
            bundle.putString("id", id)
            findNavController()
                .navigate(R.id.action_changeProgramFragment_to_infoStudentFragment2, bundle)
        }
        return view
    }
    override fun generateAdapter(map: Map<String, List<Int>>): MutableLiveData<MutableMap<String, List<Element>>> {
        val maps = mutableMapOf<String, List<Element>>()
        val mutableMap = MutableLiveData(maps)
        repository.getAllElements(map).observe(viewLifecycleOwner) { it1 ->
            maps.putAll(it1 as Map<out String, List<Element>>)
            mutableMap.postValue(maps)
        }
        return mutableMap
    }

}