package com.castprogramms.balamutbatut.ui.group

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.CreateGroupDialogBinding
import com.castprogramms.balamutbatut.databinding.GroupsFragmentBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Group
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.addgroup.ColorAdapter
import com.castprogramms.balamutbatut.ui.group.adapters.GroupsAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupsFragment : Fragment(R.layout.groups_fragment) {
    val viewModel: GroupViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = GroupsFragmentBinding.bind(view)
        requireActivity().setTitle(R.string.item_groups)
        val groupAdapter = GroupsAdapter({ group: Group, groupID: String ->
            viewModel.updateDataGroup(
                group,
                groupID
            )
        }, { viewModel.getGroupInfo(it) }, { viewModel.deleteGroup(it) })
        {viewModel.getFullNameTrainer(it)}
        viewModel.getGroups().observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {
                    binding.progressGroups.root.visibility = View.GONE
                }

                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    binding.progressGroups.root.visibility = View.GONE
                    if (it.data != null){
                        groupAdapter.setData(it.data)
                        if (it.data.isNotEmpty()) {
                            binding.noStudents.visibility = View.GONE
                        }
                        else{
                            binding.noStudents.visibility = View.VISIBLE
                            binding.noStudents.text = "Нет групп"
                        }
                    }
                }
            }
        })

        binding.groupsList.adapter = groupAdapter
        binding.groupsList.layoutManager = LinearLayoutManager(requireContext())
        binding.fabAddGroup.setOnClickListener {
            createDialogForCreateGroup()
        }
    }

    private fun createDialogForCreateGroup(){
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.create_group_dialog, null)
        val binding = CreateGroupDialogBinding.bind(view)

        val ad = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        if (ad.window != null)
            ad.window!!.setBackgroundDrawable(ColorDrawable(0))

        val adapter = ColorAdapter()
        binding.colors.adapter = adapter

        ad.show()
        binding.button.setOnClickListener {
            if (validateCreateGroup(binding)) {
                viewModel.createGroup(
                    Group(
                        binding.nameGroup.text.toString(),
                        binding.descGroup.text.toString(),
                        User.id
                    ).apply {
                        if (adapter.positionChosenColor != -1)
                            this.color =
                                resources.getColor(adapter.colors[adapter.positionChosenColor])
                    }
                )
                ad.dismiss()
            }
        }
    }

    private fun validateCreateGroup(binding: CreateGroupDialogBinding) = !mutableListOf(
            isValidEditText(binding.nameGroup, binding.nameGroupContainer),
            isValidEditText(binding.descGroup, binding.descGroupContainer)
    ).contains(false)

    private fun isValidEditText(editText: TextInputEditText, editTextContainer: TextInputLayout): Boolean {
        return if (editText.text.isNullOrEmpty() || editText.text.isNullOrBlank()){
            editTextContainer.error = "Поле не должно быть пустым"
            false
        } else{
            true
        }
    }
}