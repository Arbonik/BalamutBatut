package com.castprogramms.balamutbatut.ui.infostudent

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentInfoFragmentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoStudentFragment : FragmentWithElement(R.layout.fragment_info_fragment) {
    private var idStudent = ""
    private val viewModel: InfoStudentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (arguments != null) {
            if (arguments != null) {
                idStudent = arguments?.getString("id", "").toString()
                if (idStudent != "null" && idStudent != "") {
                    viewModel.loadUserData(idStudent)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentInfoFragmentBinding.bind(view)
        val adapterList = IHopeThisAdapterCanWork(true)
        binding.listView.adapter = adapterList
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getTrueOrder().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
//                    adapterList.filters = it.data!!
                }
            }
        })
        viewModel.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.profileInfo.person = it
                Log.e("data", it.toString())
                generateAdapter(it.element)
                viewModel.getGroupName(it.groupID)
                    .addOnSuccessListener {
                        if (it.getString("name") != null) {
                            binding.profileInfo.groupID.text = it.getString("name")
                        }
                    }
                if (it.img != "" && it.img != "null")
                    Glide.with(requireContext())
                        .load(it.img)
                        .error(R.drawable.male_user)
                        .into(binding.profileInfo.icon)
            }
        })
        mutableLiveData.observe(viewLifecycleOwner){
            adapterList.setElement(it)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.change_group_programm, menu)
        val changeProgram = menu.findItem(R.id.change_program)
        changeProgram.setOnMenuItemClickListener {
            val bundle = Bundle()
            bundle.putString("id", idStudent)
            bundle.putStringArray(
                "idElements",
                viewModel.mutableLiveDataStudent.value?.elements?.let { convertToIDsList(it) })
//            findNavController()
//                .navigate(R.id.action_infoStudentFragment_to_changeProgramFragment, bundle)
            true
        }
    }
    //del?
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.change_program -> {
//                val bundle = Bundle()
//                bundle.putString("id", idStudent)
//                bundle.putStringArray(
//                    "idElements",
//                    viewModel.mutableLiveDataStudent.value?.elements?.let { convertToIDsList(it) })
//                findNavController()
//                    .navigate(R.id.action_infoStudentFragment_to_changeProgramFragment, bundle)
//            }
//        }
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    fun convertToIDsList(elements: List<Element>): Array<String> {
        val mutableListElements = mutableListOf<String>()
        elements.forEach {
            mutableListElements.add(it.name)
        }
        return mutableListElements.toTypedArray()
    }

}