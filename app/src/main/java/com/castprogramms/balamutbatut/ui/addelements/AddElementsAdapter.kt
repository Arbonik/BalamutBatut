package com.castprogramms.balamutbatut.ui.addelements

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemAddElementsBinding
import com.castprogramms.balamutbatut.tools.Element

class AddElementsAdapter(val color: Int): RecyclerView.Adapter<AddElementsAdapter.AddElementsViewHolder>() {
    val liveDataSelectedElements = MutableLiveData<MutableList<Element>>(mutableListOf())
    fun addNeedElement(element: Element){
        if (liveDataSelectedElements.value.isNullOrEmpty())
            liveDataSelectedElements.postValue(mutableListOf(element))
        else
            liveDataSelectedElements.postValue(liveDataSelectedElements.value.apply {
                this?.add(element)
            })
    }
    fun deleteNeedElement(element: Element){
        if (liveDataSelectedElements.value?.contains(element) == true)
            liveDataSelectedElements.postValue(liveDataSelectedElements.value.apply {
                this?.remove(element)
            })
    }


    var elements = mutableListOf<Element>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddElementsViewHolder {
        return AddElementsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_elements, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddElementsViewHolder, position: Int) {
        holder.bind(elements[position])
    }

    override fun getItemCount() = elements.size

    inner class AddElementsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemAddElementsBinding.bind(view)
        fun bind(element: Element) {
            if (color != 0)
                binding.colorGroupElement.backgroundTintList = ColorStateList.valueOf(color)
            binding.nameGroupElements.text = element.name
            binding.root.setOnClickListener {
                binding.isNeed.setChecked(!binding.isNeed.isChecked, true)
                checkCheckBox(element, binding.isNeed.isChecked)
            }
            binding.isNeed.setOnClickListener {
                binding.isNeed.setChecked(!binding.isNeed.isChecked, true)
                checkCheckBox(element, binding.isNeed.isChecked)
            }
        }

        private fun checkCheckBox(element: Element, isChecked: Boolean){
            if (isChecked)
                addNeedElement(element)
            else
                deleteNeedElement(element)
        }
    }
}