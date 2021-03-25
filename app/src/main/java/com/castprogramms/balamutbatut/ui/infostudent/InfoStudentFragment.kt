package com.castprogramms.balamutbatut.ui.infostudent

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.ElementsAdapter
import com.castprogramms.balamutbatut.users.Student
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject

class InfoStudentFragment: Fragment() {
    private val repository : Repository by inject()
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
        this.setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_info_fragment, container, false)
        val binding = ProfileBinding.bind(view.findViewById(R.id.profile_info))
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_achi)
        val adapter = ElementsAdapter(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.person = it
                repository.getElement(it.elements).observe(viewLifecycleOwner, Observer {
                    adapter.setElement(it)
                })
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.change_group_programm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.change_program ->{
                val bundle = Bundle()
                bundle.putString("id", idStudent)
                findNavController()
                    .navigate(R.id.action_infoStudentFragment_to_changeProgramFragment, bundle)
            }
        }
        return true
    }
}