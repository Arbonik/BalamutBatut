package com.castprogramms.balamutbatut.ui.infostudent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.users.Student
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class InfoStudentFragment: Fragment() {
    var student : Student? = null
    val mutableLiveDataStudent = MutableLiveData(student)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            val id = arguments?.getString("id", null)
            if (id != null){
                DataUserFirebase().getStudent(id).addSnapshotListener { value, error ->
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
        val binding = ProfileBinding.bind(view.findViewById(R.id.profile_info))
        mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.person = it
                DataUserFirebase().getNameGroup(it.groupID)
                    .addSnapshotListener { value, error ->
                        Log.e("Photo", value?.data.toString())
                        if (value != null) {
                            binding.groupID.text = value.getString("name")
                            Log.e("Photo", value.getString("name").toString())
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