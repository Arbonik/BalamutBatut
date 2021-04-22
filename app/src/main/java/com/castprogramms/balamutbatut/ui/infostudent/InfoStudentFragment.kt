package com.castprogramms.balamutbatut.ui.infostudent

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.databinding.FragmentInfoFragmentBinding
import com.castprogramms.balamutbatut.databinding.ProfileBinding
import com.castprogramms.balamutbatut.graph.Line
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.ElementsAdapter
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import com.castprogramms.balamutbatut.ui.rating.ExpandableList
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class InfoStudentFragment: FragmentWithElement() {
    var idStudent = ""
    private val viewModel : InfoStudentViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null){
            idStudent = arguments?.getString("id", "").toString()
            if (idStudent != "null" && idStudent != ""){
                viewModel.loadUserData(idStudent)
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
        val binding = FragmentInfoFragmentBinding.bind(view)
        val adapterList = IHopeThisAdapterCanWork(viewLifecycleOwner, true)
        binding.listView.adapter = adapterList
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.profileInfo.person = it
                Log.e("data", it.element.toString())

                generateAdapter(it.element).observe(viewLifecycleOwner){
                    adapterList.setElement(it)
                }
                DataUserFirebase().getNameGroup(it.groupID)
                    .addSnapshotListener { value, error ->
                        if (value != null) {
                            binding.profileInfo.groupID.text = value.getString("name")
                        }
                    }
                if (it.img != "" && it.img != "null")
                Picasso.get()
                    .load(it.img)
                    .into(binding.profileInfo.icon)
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
                bundle.putStringArray("idElements", viewModel.mutableLiveDataStudent.value?.elements?.let { convertToIDsList(it) })
                findNavController()
                    .navigate(R.id.action_infoStudentFragment_to_changeProgramFragment, bundle)
            }
        }
        return true
    }

    fun convertToIDsList(elements: List<Element>): Array<String> {
        val mutableListElements = mutableListOf<String>()
        elements.forEach {
            mutableListElements.add(it.name)
        }
        return mutableListElements.toTypedArray()
    }
}