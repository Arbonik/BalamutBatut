package com.castprogramms.balamutbatut.ui.changeprogram.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemAddElementBinding
import com.castprogramms.balamutbatut.tools.Element

class IHopeThisAdapterCanWork(val isProfile: Boolean = false) : RecyclerView.Adapter<IHopeThisAdapterCanWork.AddElementsViewHolder>() {
    var elements = mutableListOf<Pair<String, List<Element>>>()
    val checkedElements = mutableMapOf<String, List<Element>>()
    var filters = listOf<String>()
    set(value) {
        field = value
        filter()
    }
    fun setElement(map: Map<String, List<Element>>) {
        elements.clear()
        map.forEach {
            elements.add(it.key to it.value)
        }
        Log.e("data", map.toString())
        if (filters.isNotEmpty())
            filter()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddElementsViewHolder {
        return AddElementsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddElementsViewHolder, position: Int) {
        holder.onBind(elements[position])
    }

    override fun getItemCount() = elements.size

    inner class AddElementsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemAddElementBinding.bind(view)
        fun onBind(elements: Pair<String, List<Element>>) {
            binding.titleElements.text = elements.first
            binding.groupElements.layoutManager = LinearLayoutManager(itemView.context)
            val adapter = ElementsAdapter(itemView.context, isProfile).apply {
                setElement(elements.second)
            }
            adapter.mutableLiveDataElements.observeForever {
                if (it.isNotEmpty())
                    checkedElements[elements.first] = it
            }
            binding.groupElements.adapter = adapter
            binding.arrowBtn.setOnClickListener {
                if (binding.expandableView.visibility == View.GONE) {
                    val anim =
                        AnimationUtils.loadAnimation(itemView.context, R.anim.show)
                    anim.interpolator = FastOutSlowInInterpolator()
                    binding.expandableView.startAnimation(anim)
                    anim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            binding.expandableView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                        }
                    })
                    binding.arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                } else {
                    val anim =
                        AnimationUtils.loadAnimation(itemView.context, R.anim.check_item_anim)
                    anim.interpolator = FastOutSlowInInterpolator()
                    binding.expandableView.startAnimation(anim)
                    anim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            binding.expandableView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            binding.expandableView.visibility = View.GONE
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                        }
                    })
                    binding.arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                }
            }

            binding.root.setOnClickListener {
                if (binding.expandableView.visibility == View.GONE) {
                    val anim =
                        AnimationUtils.loadAnimation(itemView.context, R.anim.show)
                    binding.expandableView.startAnimation(anim)
                    anim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            binding.expandableView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                        }
                    })
                    binding.arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                } else {
                    val anim =
                        AnimationUtils.loadAnimation(itemView.context, R.anim.check_item_anim)
                    binding.expandableView.startAnimation(anim)
                    anim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            binding.expandableView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            binding.expandableView.visibility = View.GONE
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                        }
                    })
                    binding.arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                }
            }
        }
    }

    private fun filter() {
        val list = MutableList<Pair<String, List<Element>>>(filters.size) { Pair("", listOf()) }
        val map = getListAndPosition(filters)
        elements.forEach {
            list[map[it.first]!!] = it
        }
        for (i in list.indices.reversed())
            if (list[i].first == "" && list[i].second.isEmpty())
                list.removeAt(i)
        elements = list
    }

    fun getListAndPosition(list: List<String>): MutableMap<String, Int> {
        val map = mutableMapOf<String, Int>()
        list.forEachIndexed { index, s ->
            map.put(s, index)
        }
        return map
    }
}
