package com.castprogramms.balamutbatut.ui.showelements

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemGroupElementBinding

class ShowGroupElementsAdapter(val idStudent: String) : RecyclerView.Adapter<ShowGroupElementsAdapter.GroupElementsViewHolder>() {
    var elementsTitle = mutableListOf<Pair<String, Int>>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupElementsViewHolder {
        return GroupElementsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_group_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupElementsViewHolder, position: Int) {
        holder.bind(elementsTitle[position])
    }

    override fun getItemCount() = elementsTitle.size

    inner class GroupElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemGroupElementBinding.bind(view)
        fun bind(pair: Pair<String, Int>){
            binding.colorGroupElement.backgroundTintList = ColorStateList.valueOf(pair.second)
            binding.nameGroupElements.text = pair.first
            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", idStudent)
                bundle.putString("title", pair.first)
                bundle.putInt("color", pair.second)
                it.findNavController()
                    .navigate(R.id.action_showElementsFragment_to_showElementsFragment2, bundle)
            }
        }
    }
}