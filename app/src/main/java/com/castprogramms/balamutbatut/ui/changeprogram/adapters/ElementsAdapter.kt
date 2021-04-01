package com.castprogramms.balamutbatut.ui.changeprogram.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding.getColorFromResource
import com.castprogramms.balamutbatut.databinding.ProfileBinding.inflate
import com.castprogramms.balamutbatut.tools.Element
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class ElementsAdapter(val context: Context, val isProfile: Boolean):
    RecyclerView.Adapter<ElementsAdapter.ElementsViewHolder>() {
    private var lastPosition = -1
    var elements = mutableListOf<Element>()
    var checkedElements = mutableListOf<Element>()

    fun addElement(element: Element){
        elements.add(element)
        notifyDataSetChanged()
    }
    fun addElement(elements: List<Element>){
        this.elements.addAll(elements)
        notifyDataSetChanged()
    }
    fun setElement(element: List<Element>){
        elements = element.toMutableList()
        notifyDataSetChanged()
    }
    fun deleteElement(elements: List<Element>){
        elements.minus(elements)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder {
        return if(isProfile){
            ElementsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.element_recycler, parent, false))
        } else{
            ElementsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.element_check_recycler, parent, false))
        }
    }
    override fun onBindViewHolder(holder: ElementsViewHolder, position: Int) {
        holder.bind(elements[position])
    }
    override fun getItemCount() = elements.size
    inner class ElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardView : MaterialCardView = view.findViewById(R.id.cardView_element)
        val textElement : MaterialTextView = view.findViewById(R.id.text_element)
        val checkbox : MaterialCheckBox by lazy { view.findViewById(R.id.checkbox)}
        fun bind(element: Element){
            textElement.text = element.name
            cardView.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
                if (checkbox.isChecked){
                    if (!isProfile) cardView.setBackgroundColor(Color.rgb(216, 243, 232))
                    checkedElements.add(element)
                }
                else {
                    if (!isProfile) cardView.setBackgroundColor(Color.rgb(255, 255, 255))
                    checkedElements.remove(element)
                }
            }
            when(isProfile) {
                false -> {
                    checkbox.setOnClickListener {
                        if (checkbox.isChecked) {
                            cardView.setBackgroundColor(Color.rgb(216, 243, 232))
                            checkedElements.add(element)
                        }
                        else {
                            checkedElements.remove(element)
                            cardView.setBackgroundColor(Color.rgb(255, 255, 255))
                        }
                    }
                }
                true -> {

                }
            }
        }
    }
}