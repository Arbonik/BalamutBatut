package com.castprogramms.balamutbatut.ui.changeprogram.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.provider.Settings.System.getConfiguration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ProfileBinding.getColorFromResource
import com.castprogramms.balamutbatut.databinding.ProfileBinding.inflate
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.ui.changeprogram.ChangeElementsStudentFragment
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
        val checkbox : MaterialCheckBox = view.findViewById(R.id.checkbox)
        //val themee = MainActivityStudent().theme.resources.configuration.uiMode
        @SuppressLint("ResourceAsColor")
        fun bind(element: Element) {
            textElement.text = element.name
            cardView.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
                if (checkbox.isChecked) {
                    if (!isProfile) {
//                        when (uiMode){
//                            Configuration.UI_MODE_NIGHT_NO -> {
                        cardView.setCardBackgroundColor(Color.rgb(216, 243, 232))
//                            }
//                            Configuration.UI_MODE_NIGHT_YES -> {
                        //cardView.setCardBackgroundColor(Color.rgb(40, 40, 40))
//                            }
                    }

                    checkedElements.add(element)
                }
                else {
                if (!isProfile) {
//                        when (themee){
//                            Configuration.UI_MODE_NIGHT_NO -> {
                    cardView.setCardBackgroundColor(Color.argb(0, 255, 255, 255))
//                            }
//                            Configuration.UI_MODE_NIGHT_YES -> {
                    //cardView.setCardBackgroundColor(Color.rgb(0, 0, 0))
//                            }
                }
            }
                checkedElements.remove(element)
            }

            when(isProfile) {
                false -> {
                    checkbox.setOnClickListener {
                        if (checkbox.isChecked) {
                            cardView.setCardBackgroundColor(Color.rgb(216, 243, 232))//(R.color.message_color)//(R.style.CheckItem)//(Color.rgb(216, 243, 232))
                            checkedElements.add(element)
                        }
                        else {
                            checkedElements.remove(element)
                            cardView.setCardBackgroundColor(Color.rgb(255, 255, 255))//(R.color.white)//(R.style.NotCheckItem)
                        }
                    }
                }
                true -> {

                }
            }
        }
    }
    fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}