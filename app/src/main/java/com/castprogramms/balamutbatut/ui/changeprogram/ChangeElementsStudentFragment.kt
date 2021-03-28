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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.ElementsAdapter
import com.castprogramms.balamutbatut.ui.registr.RegistrViewModel
import com.castprogramms.balamutbatut.users.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject

class ChangeElementsStudentFragment: Fragment() {
    private val repository: Repository by inject()
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
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerElements)
        val checkFab : FloatingActionButton = view.findViewById(R.id.check_fab)
        val adapter = ElementsAdapter(requireContext(),false )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null){
                adapter.deleteElement(it.elements)
            }
        })
        changeElementsViewModel.getNamesElements(repository, idElements)
            .observe(viewLifecycleOwner, Observer {
                adapter.setElement(it)
            })
        checkFab.setOnClickListener {
            Log.e("data", adapter.checkedElements.toString())
            adapter.checkedElements.forEach {
                repository.updateElementsStudent(it, id)
            }
            val bundle = Bundle()
            bundle.putString("id", id)
            findNavController()
                .navigate(R.id.action_changeProgramFragment_to_infoStudentFragment2, bundle)
        }
        return view
    }

}