package com.castprogramms.balamutbatut.ui.allElements

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemGroupElementBinding
import com.castprogramms.balamutbatut.databinding.ListItemBinding
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.ui.groupelements.GroupElementsAdapter

class AllElementsAdapter: RecyclerView.Adapter<AllElementsAdapter.AllElementsViewHolder>(){
    var elementsTitle = mutableListOf<Pair<String, Int>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllElementsViewHolder {
        return AllElementsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_group_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AllElementsViewHolder, position: Int) {
        holder.bind(elementsTitle[position])
    }

    override fun getItemCount() = elementsTitle.size


    inner class AllElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemGroupElementBinding.bind(view)
        fun bind(pair: Pair<String, Int>){
            binding.colorGroupElement.backgroundTintList = ColorStateList.valueOf(pair.second)
            binding.nameGroupElements.text = pair.first
            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", pair.first)
                bundle.putInt("color", pair.second)
                it.findNavController()
                    .navigate(R.id.action_allElementListFragment2_to_allGroupElementFragment, bundle)
            }
        }
    }
}
