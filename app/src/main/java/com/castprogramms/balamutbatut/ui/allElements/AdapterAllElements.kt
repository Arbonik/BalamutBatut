package com.castprogramms.balamutbatut.ui.allElements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ListItemBinding
import com.castprogramms.balamutbatut.tools.Element

class AdapterAllElements(allElement: MutableList<Int>):
    RecyclerView.Adapter<AdapterAllElements.AllElementsViewHolder>(){

    var listE = listOf<Element>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllElementsViewHolder {
        return AllElementsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AllElementsViewHolder, position: Int) {
        holder.onBind(listE[position])
    }

    override fun getItemCount(): Int = listE.size

    inner class AllElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ListItemBinding.bind(view)
        fun onBind(element: Element) {
            binding.expandedListItem.text = element.name
        }
    }
}
