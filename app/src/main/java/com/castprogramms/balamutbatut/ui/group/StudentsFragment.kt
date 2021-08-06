package com.castprogramms.balamutbatut.ui.group

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.StudentsFragmentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.SimpleItemTouchHelperCallback
import com.castprogramms.balamutbatut.ui.group.adapters.StudentsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentsFragment: Fragment(R.layout.students_fragment) {
    var id = ""
    var nameGroup = ""
    val viewModel :StudentsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = arguments?.getString("id").toString()
            nameGroup = if (arguments?.getString("name") != null)
                arguments?.getString("name")!! else ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = StudentsFragmentBinding.bind(view)
        requireActivity().title = nameGroup
        binding.fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", id)
            findNavController().navigate(R.id.action_studentsFragment_to_addStudentFragment, bundle)
        }
        val recyclerView : RecyclerView = view.findViewById(R.id.students_list)
        val studentsAdapter = StudentsAdapter(id,
                {studentId: String, groupId: String -> viewModel.deleteStudent(studentId, groupId)},
                {viewModel.getSortedElements() })
                { studentId: String -> viewModel.getStudentElements(studentId) }
        viewModel.getStudents(id).observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> binding.studentsList.hideShimmer()
                is Resource.Loading -> {
                    binding.studentsList.showShimmer()
                }
                is Resource.Success -> {
                    binding.studentsList.hideShimmer()
                    if (it.data != null) {
                        studentsAdapter.setData(it.data)
                        if (it.data.isEmpty()){
                            binding.noStudents.visibility = View.VISIBLE
                            binding.noStudents.text = "Нет учеников в группе"
                        }
                        else{
                            binding.noStudents.visibility = View.GONE
                        }
                    }
                }
            }
        })
        recyclerView.adapter = studentsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val callback = SimpleItemTouchHelperCallback(studentsAdapter)
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

}