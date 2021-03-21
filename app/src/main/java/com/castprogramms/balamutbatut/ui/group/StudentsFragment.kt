package com.castprogramms.balamutbatut.ui.group

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.group.adapters.StudentsAdapter
import com.castprogramms.balamutbatut.users.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.gson.Gson
import java.lang.Exception

class StudentsFragment: Fragment() {
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            id = arguments?.getString("id").toString()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.students_fragment, container, false)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_studentsFragment_to_addStudentFragment)
        }
        val recyclerView : RecyclerView = view.findViewById(R.id.students_list)
        val query = DataUserFirebase().fireStore.collection(DataUserFirebase.studentTag)
            .whereEqualTo("type", "student")
            .whereEqualTo("groupID", id)
        val studentsAdapter = StudentsAdapter(query)
        recyclerView.adapter = studentsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

}