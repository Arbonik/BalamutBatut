package com.castprogramms.balamutbatut.ui.addstudents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.addstudents.adapters.AddStudentAdapter
import com.castprogramms.balamutbatut.ui.group.adapters.StudentsAdapter
import com.castprogramms.balamutbatut.users.Person

class AddStudentFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val query = DataUserFirebase().fireStore.collection("students")
            .whereEqualTo("groupID", Person.notGroup)
            .whereEqualTo("type", "student")

        val view = inflater.inflate(R.layout.fragment_add_student, container, false)
        val studentsAdapter = AddStudentAdapter(query)
        val recycler : RecyclerView = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = studentsAdapter

        return view
    }
}