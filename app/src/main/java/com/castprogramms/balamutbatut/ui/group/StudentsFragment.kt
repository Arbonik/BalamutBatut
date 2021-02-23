package com.castprogramms.balamutbatut.ui.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.group.adapters.StudentsAdapter
import java.lang.Exception

class StudentsFragment: Fragment() {
    var group : Group? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = requireArguments().getString("name").toString()
        val desc = requireArguments().get("description").toString()
        val number = requireArguments().getString("numberTrainer").toString()
        val students = requireArguments().getStringArray("students")?.toList()!!

        group = Group(name, desc, number, students)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val query = DataUserFirebase().fireStore.collection(DataUserFirebase.studentTag)
        val view = inflater.inflate(R.layout.students_fragment, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.students_list)
        if (group != null) {
            val studentsAdapter = StudentsAdapter(query, group!!)
            recyclerView.adapter = studentsAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        return view
    }

}