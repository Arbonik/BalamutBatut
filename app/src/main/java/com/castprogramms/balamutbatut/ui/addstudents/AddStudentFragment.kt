package com.castprogramms.balamutbatut.ui.addstudents

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentAddStudentBinding
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.ui.addstudents.adapters.AddStudentAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddStudentFragment: Fragment(R.layout.fragment_add_student) {
    val viewModel : AddStudentViewModel by viewModel()
    var id = ""
    val studentsAdapter = AddStudentAdapter(id)
        { studentId: String, groupID: String -> viewModel.updateStudentGroup(studentId, groupID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = requireArguments().getString("id", "")
        studentsAdapter.idGroup = id
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.setHasOptionsMenu(true)
        val binding = FragmentAddStudentBinding.bind(view)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = studentsAdapter
        viewModel.getStudentWithoutGroup().observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {
                    binding.progress.progressBar.visibility = View.GONE
                    Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.progress.progressBar.visibility = View.GONE
                    if (it.data != null){
                        if (it.data.isNotEmpty()) {
                            studentsAdapter.setData(it.data)
                            binding.noStudents.visibility = View.GONE
                        }
                        else
                            binding.noStudents.text = "Нет студентов без групп"
                    }
                }
            }
        })

        studentsAdapter.liveDataSelectedStudent.observe(viewLifecycleOwner, {
            Log.e("it", it.toString())
            if (it.isNotEmpty())
                binding.addStudent.visibility = View.VISIBLE
            else
                binding.addStudent.visibility = View.INVISIBLE
        })

        binding.addStudent.setOnClickListener {
            if (!studentsAdapter.liveDataSelectedStudent.value.isNullOrEmpty()) {
                studentsAdapter.liveDataSelectedStudent.value?.forEach {
                    viewModel.updateStudentGroup(it, id)
                }
                it.findNavController().popBackStack()
            }
        }
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