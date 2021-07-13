package com.castprogramms.balamutbatut.ui.addgroup

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemColorBinding

class ColorAdapter(groupsId: MutableList<String>) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    val colors = listOf(R.color.firstColor, R.color.secondColor, R.color.thirdColor,
        R.color.fourthColor, R.color.fifthColor, R.color.sixthColor)
    var positionChosenColor = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_color, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], position)
    }

    override fun getItemCount() = colors.size

    inner class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemColorBinding.bind(view)
        fun bind(color: Int, position: Int) {
            binding.color.setImageDrawable(ColorDrawable(itemView.context.resources.getColor(color)))
            if (position == positionChosenColor)
                binding.selectColor.visibility = View.VISIBLE
            else
                binding.selectColor.visibility = View.GONE
            binding.root.setOnClickListener {
                this@ColorAdapter.positionChosenColor = position
                this@ColorAdapter.notifyDataSetChanged()
            }
        }
    }
}