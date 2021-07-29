package com.castprogramms.balamutbatut.ui.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.GroupsFragmentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Group
import com.castprogramms.balamutbatut.ui.group.adapters.GroupsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupsFragment : Fragment(R.layout.groups_fragment) {
    val viewModel: GroupViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = GroupsFragmentBinding.bind(view)
        val groupAdapter = GroupsAdapter({ group: Group, groupID: String ->
            viewModel.updateDataGroup(
                group,
                groupID
            )
        }, { viewModel.getGroupInfo(it) })
        { viewModel.deleteGroup(it) }
        viewModel.getGroups().observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {
                    binding.progressGroups.root.visibility = View.GONE
                }

                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    binding.progressGroups.root.visibility = View.GONE
                    if (it.data != null)
                        groupAdapter.setData(it.data)
                }
            }
        })

        binding.groupsList.adapter = groupAdapter
        binding.groupsList.layoutManager = LinearLayoutManager(requireContext())
        binding.fabAddGroup.setOnClickListener {
            findNavController().navigate(R.id.action_group_Fragment_to_addGroupFragment)
        }
    }
}