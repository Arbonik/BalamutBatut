package com.castprogramms.balamutbatut.ui.group.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R

class ElementsStudentAdapter: RecyclerView.Adapter<ElementsStudentAdapter.ElementStudentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementStudentViewHolder {
        return ElementStudentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_progress_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ElementStudentViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 6

    inner class ElementStudentViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(){

        }
    }
}