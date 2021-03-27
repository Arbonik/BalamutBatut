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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.ElementsAdapter
import com.castprogramms.balamutbatut.ui.registr.RegistrViewModel
import com.castprogramms.balamutbatut.users.Student
import org.koin.android.ext.android.inject

class ChangeElementsStudentFragment: Fragment() {
    private val repository: Repository by inject()
    var student : Student? = null
    var id = ""
    val mutableLiveDataStudent = MutableLiveData(student)
    val changeElementsViewModel: ChangeElementsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            id = arguments?.getString("id").toString()
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
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerElements)
        val adapter = ElementsAdapter(false )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null){
                changeElementsViewModel.getNamesElements(it.elements, repository)
                    .observe(viewLifecycleOwner, Observer {
                      adapter.setElement(it)
                    })
            }
        })

        return view
    }

}