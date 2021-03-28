package com.castprogramms.balamutbatut.ui.group

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.group.adapters.GroupsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupsFragment : Fragment() {
    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.groups_fragment, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.groups_list)
        val addGroupFloatingActionButton: FloatingActionButton =
            view.findViewById(R.id.fab_add_group)
        val groupAdapter = GroupsAdapter(DataUserFirebase().getGroups(), this)
        recyclerView.adapter = groupAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        addGroupFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_group_Fragment_to_addGroupFragment)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
    }

}