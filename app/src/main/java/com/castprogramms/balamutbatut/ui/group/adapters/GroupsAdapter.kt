package com.castprogramms.balamutbatut.ui.group.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class GroupsAdapter(_query:Query, var fragment: Fragment): RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder>() {
    var groups = mutableListOf<Group>()
    var groupsId = mutableListOf<String>()

    var query = _query
    set(value) {
        notifyDataSetChanged()
        field = value
    }
    init {

        query.addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                update()
                value?.documents?.forEach {
                    Log.e("Data", it.data.toString())
                    groups.add(
                        Group(
                            it.getString("name").toString(),
                            it.getString("description").toString(),
                            it.getString("numberTrainer").toString(),
                            it.get("students") as List<String>
                        )
                    )
                    groupsId.add(it.id)
                    notifyDataSetChanged()
                }
            }
        })
    }

    fun update(){
        groups.clear()
        groupsId.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        return GroupsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.group_adapter, parent, false
            ),
            fragment
        )
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(groups[position], groupsId[position])
    }

    override fun getItemCount() = groups.size

    inner class GroupsViewHolder(view: View, fragment: Fragment): RecyclerView.ViewHolder(view){
        val groupName : TextView = view.findViewById(R.id.group_name)
        val groupDesc : TextView = view.findViewById(R.id.group_desc)
        val groupTrainerNumber : TextView = view.findViewById(R.id.group_trainerNumber)
        val cardView : CardView = view.findViewById(R.id.cardView)
        fun bind(group: Group, id: String){
            groupName.text = group.name
            groupDesc.text = group.description
            groupTrainerNumber.text = "${group.numberTrainer} ${group.students.size}"
            cardView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("name", group.name)
                bundle.putString("description", group.description)
                bundle.putString("numberTrainer", group.numberTrainer)
                bundle.putStringArray("students",group.students.toTypedArray())
                bundle.putString("id", id)
                fragment.findNavController()
                    .navigate(R.id.action_group_Fragment_to_studentsFragment, bundle)
            }
        }
    }
}