package com.castprogramms.balamutbatut.ui.group.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemProgressUserBinding
import com.castprogramms.balamutbatut.tools.Element

class ElementsStudentAdapter: RecyclerView.Adapter<ElementsStudentAdapter.ElementStudentViewHolder>() {
    var userElements = mutableListOf<Pair<String, List<Int>>>()
    set(value) {
        field = value
        Log.e("prog", userElements.toString())
        if (allElements.isNotEmpty())
            notifyDataSetChanged()
    }
    var allElements = mutableListOf<Pair<String, List<Element>>>()
    set(value) {
        field = value
        if (userElements.isNotEmpty())
            notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementStudentViewHolder {
        return ElementStudentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_progress_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ElementStudentViewHolder, position: Int) {
        val curElement = allElements[position]
        val curUserElement = userElements.find{
            it.first == curElement.first
        }
        if (curUserElement == null)
            holder.bind(curElement.first to listOf(), curElement)
        else
            holder.bind(curUserElement, curElement)
    }

    override fun getItemCount() = allElements.size

    inner class ElementStudentViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemProgressUserBinding.bind(view)
        fun bind(pairStudent: Pair<String, List<Int>>, pairAll: Pair<String, List<Element>>) {
            binding.titleElement.text = pairAll.first
            val process = (pairStudent.second.size.toDouble() / pairAll.second.size.toDouble()) * 100
            binding.progressBarElement.progress = process.toInt()
        }
    }
}