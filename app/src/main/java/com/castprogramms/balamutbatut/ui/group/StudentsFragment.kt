package com.castprogramms.balamutbatut.ui.group

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.MainActivityTrainer
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.group.adapters.StudentsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject

class StudentsFragment: Fragment() {
    var id = ""
    var nameGroup = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = arguments?.getString("id").toString()
            nameGroup = if (arguments?.getString("name") != null)
                arguments?.getString("name")!! else ""
        }
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