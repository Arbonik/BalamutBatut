package com.castprogramms.balamutbatut.ui.changeprogram.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.Element
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class ElementsAdapter: RecyclerView.Adapter<ElementsAdapter.ElementsViewHolder>() {
    var elements = mutableListOf<Element>()
    var checkedElements = mutableListOf<Element>()

    fun addElement(element: Element){
        elements.add(element)
        notifyDataSetChanged()
    }
    fun addElement(elements:List<Element>){
        this.elements.addAll(elements)
        notifyDataSetChanged()
    }
    fun setElement(element: List<Element>){
        elements = element.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder {
        return ElementsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.element_recycler, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ElementsViewHolder, position: Int) {
        holder.bind(elements[position])
    }

    override fun getItemCount() = elements.size

    inner class ElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardView : MaterialCardView = view.findViewById(R.id.cardView_element)
        val textElement : MaterialTextView = view.findViewById(R.id.text_element)
        val checkbox : MaterialCheckBox = view.findViewById(R.id.checkbox)
        fun bind(element: Element){
            textElement.text = element.name
            cardView.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
                if (checkbox.isChecked)
                    checkedElements.add(element)
                else
                    checkedElements.remove(element)
            }
            checkbox.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
                if (checkbox.isChecked)
                    checkedElements.add(element)
                else
                    checkedElements.remove(element)
            }
        }
    }
}