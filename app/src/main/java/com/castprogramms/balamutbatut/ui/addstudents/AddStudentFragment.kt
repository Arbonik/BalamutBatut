package com.castprogramms.balamutbatut.ui.addstudents

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.ui.addstudents.adapters.AddStudentAdapter
import org.koin.android.ext.android.inject

class AddStudentFragment: Fragment() {
    private val repository : Repository by inject()
    var id = ""
    val query = repository.getCollectionAllStudentsWithoutGroup()
    val studentsAdapter = AddStudentAdapter(query, id, repository)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            id = arguments?.getString("id").toString()
            studentsAdapter.idGroup = id
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_student, container, false)
        this.setHasOptionsMenu(true)
        val recycler : RecyclerView = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = studentsAdapter
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_student, menu)
        val search = menu.findItem(R.id.search_student)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.e("data", newText.toString())
                if (newText != null)
                    studentsAdapter.filter.filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}