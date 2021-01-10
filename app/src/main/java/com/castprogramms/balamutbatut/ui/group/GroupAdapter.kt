package com.castprogramms.balamutbatut.ui.group

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.GsonBuilder

class GroupAdapter(_query:Query): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {
    var groups = mutableListOf<Group>()

    var query = _query
    set(value) {
        notifyDataSetChanged()
        field = value
    }

    val gsonConverter =GsonBuilder().create()

    init {
        query.addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                update()
                value?.documents?.forEach {
                    groups.add(
                        gsonConverter.fromJson(it.data.toString(), Group::class.java)
                    )
                    notifyDataSetChanged()
                }
            }
        }
        )
    }

    fun update(){
        groups.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.group_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount() = groups.size

    inner class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){
        val groupName : TextView = view.findViewById(R.id.group_name)
        val groupDesc : TextView = view.findViewById(R.id.group_desc)
        val groupTrainerNumber : TextView = view.findViewById(R.id.group_trainerNumber)
        fun bind(group: Group){
            groupName.text = group.name
            groupDesc.text = group.description
            groupTrainerNumber.text = "${group.numberTrainer} ${group.students.size}"
        }
    }
}