package com.castprogramms.balamutbatut.ui.groupelements

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemGroupElementBinding

class GroupElementsAdapter: RecyclerView.Adapter<GroupElementsAdapter.GroupElementsViewHolder>() {
    var elementsTitle = mutableListOf<Pair<String, Number>>()
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
        holder.bind(position, elementsTitle[position])
    }

    override fun getItemCount() = elementsTitle.size

    inner class GroupElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemGroupElementBinding.bind(view)
        private val colors = view.context.resources.getStringArray(R.array.bg_color)
        private val colorsInt = mutableListOf<Int>()
        init {
            colors.forEach {
                colorsInt.add(Color.parseColor(it))
            }
            Log.e("color", colorsInt.toString())
        }

        fun bind(position: Int, pair: Pair<String, Number>){
            binding.colorGroupElement.setBackgroundColor(colorsInt[position])
            binding.nameGroupElements.text = pair.first
        }
    }
}