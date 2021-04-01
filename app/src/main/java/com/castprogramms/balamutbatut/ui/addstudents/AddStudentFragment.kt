package com.castprogramms.balamutbatut.ui.addstudents

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.ui.addstudents.adapters.AddStudentAdapter
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject

class AddStudentFragment: Fragment() {
    private val repository : Repository by inject()
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            id = arguments?.getString("id").toString()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val query = repository.getCollectionAllStudentsWithoutGroup()
        val view = inflater.inflate(R.layout.fragment_add_student, container, false)
        this.setHasOptionsMenu(true)
        val editTextSearchStudent : TextInputEditText = view.findViewById(R.id.text_search)
        editTextSearchStudent.visibility = View.GONE
        val studentsAdapter = AddStudentAdapter(query, id, repository)
        val recycler : RecyclerView = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = studentsAdapter
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_student, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search_student ->{
                
            }
        }
        return true
    }
}