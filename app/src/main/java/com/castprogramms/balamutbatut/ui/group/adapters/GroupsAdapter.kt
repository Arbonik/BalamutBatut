package com.castprogramms.balamutbatut.ui.group.adapters

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.tools.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.EditGroupDialogBinding
import com.castprogramms.balamutbatut.databinding.ItemGroupBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.ui.addgroup.ColorAdapter

class GroupsAdapter(val updateData: (Group, String) -> MutableLiveData<Resource<String>>) : RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder>() {
    var groups = mutableListOf<Group>()
    var groupsId = mutableListOf<String>()
    fun setData(data: MutableList<Pair<Group, String>>){
        groups.clear()
        groupsId.clear()
        var curGroups = mutableListOf<Group>()
        var curGroupIds = mutableListOf<String>()
        data.forEach {
            curGroups.add(it.first)
            curGroupIds.add(it.second)
        }
        curGroups = curGroups.toMutableSet().toMutableList()
        curGroupIds = curGroupIds.toMutableSet().toMutableList()
        Log.e("Data", curGroups.toString())
        Log.e("data", curGroupIds.toString())
        groups = curGroups
        groupsId = curGroupIds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        return GroupsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_group, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(groups[position], groupsId[position])
    }

    override fun getItemCount() = groups.size

    inner class GroupsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemGroupBinding.bind(view)
        fun bind(group: Group, id: String){
            if (group.color != 0)
                binding.groupCard.background = ColorDrawable(group.color)
            binding.groupName.text = group.name
            binding.groupDesc.text = group.description
            binding.quantityStudents.text = "Участники: ${group.students.size}"
            binding.groupCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("name", group.name)
                bundle.putString("id", id)
                itemView.findNavController()
                    .navigate(R.id.action_group_Fragment_to_studentsFragment, bundle)
            }
            binding.edit.setOnClickListener {
                //TODO сделать изменение данных группы
                createEditAlertDialog(group, id)
            }

            binding.add.setOnClickListener {
                //TODO сделать добавление студента или тренера
            }

            binding.delete.setOnClickListener {
                //TODO сделать удаление учеников
            }
        }

        private fun createEditAlertDialog(group: Group, groupId: String){
            val view = LayoutInflater.from(itemView.context)
                .inflate(R.layout.edit_group_dialog, null)
            val binding = EditGroupDialogBinding.bind(view)
            binding.colors.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = ColorAdapter(groupsId)
            binding.colors.adapter = adapter
            val ad = AlertDialog.Builder(itemView.context)
                .setView(view)
                .create()
            if (ad.window != null)
                ad.window!!.setBackgroundDrawable(ColorDrawable(0))
            ad.show()
            binding.button.setOnClickListener {
                val newGroup = group.apply {
                    if (!binding.nameGroup.text.isNullOrBlank())
                        this.name = binding.nameGroup.text.toString()
                    if (!binding.descGroup.text.isNullOrBlank())
                        this.name = binding.descGroup.text.toString()
                    if (adapter.positionChosenColor != -1)
                        this.color = itemView.context.resources.getColor(adapter.colors[adapter.positionChosenColor])
                }
                updateData(newGroup, groupId)
            }
        }
    }
}