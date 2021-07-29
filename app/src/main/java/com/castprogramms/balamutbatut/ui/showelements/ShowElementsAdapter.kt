package com.castprogramms.balamutbatut.ui.showelements

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemAddElementsBinding
import com.castprogramms.balamutbatut.tools.Element

class ShowElementsAdapter(val color: Int): RecyclerView.Adapter<ShowElementsAdapter.ShowElementsViewHolder>() {
    val liveDataSelectedElements = MutableLiveData<MutableList<Element>>(mutableListOf())

    var elements = mutableListOf<Element>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowElementsViewHolder {
        return ShowElementsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_elements, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShowElementsViewHolder, position: Int) {
        holder.bind(elements[position])
    }

    override fun getItemCount() = elements.size

    inner class ShowElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemAddElementsBinding.bind(view)
        fun bind(element: Element) {
            binding.isNeed.visibility = View.INVISIBLE
            if (color != 0)
                binding.colorGroupElement.backgroundTintList = ColorStateList.valueOf(color)
            binding.nameGroupElements.text = element.name
        }
    }
}