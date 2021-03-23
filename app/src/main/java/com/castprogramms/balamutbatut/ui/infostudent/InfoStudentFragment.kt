package com.castprogramms.balamutbatut.ui.infostudent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.graph.TreeGraphView
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.users.Student
import com.squareup.picasso.Picasso

class InfoStudentFragment: Fragment() {
    var student : Student? = null
    val mutableLiveDataStudent = MutableLiveData(student)
    var idStudent = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            idStudent = arguments?.getString("id", "").toString()
            if (idStudent != "null" && idStudent != ""){
                DataUserFirebase().getUser(idStudent).addSnapshotListener { value, error ->
                    if (value != null){
                        student = value.toObject(Student::class.java)
                        mutableLiveDataStudent.postValue(student)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_fragment, container, false)
        val treeGraphView : TreeGraphView = view.findViewById(R.id.model)
        val binding = ProfileBinding.bind(view.findViewById(R.id.profile_info))
        mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.person = it
                treeGraphView.idStudent = idStudent
                treeGraphView.setNodesWithInfo(it.nodes.toMutableList())
                DataUserFirebase().getNameGroup(it.groupID)
                    .addSnapshotListener { value, error ->
                        if (value != null) {
                            binding.groupID.text = value.getString("name")
                        }
                    }
                if (it.img != "")
                Picasso.get()
                    .load(it.img)
                    .into(binding.icon)
            }
        })
        return view
    }
}