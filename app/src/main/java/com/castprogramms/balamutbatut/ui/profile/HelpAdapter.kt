package com.castprogramms.balamutbatut.ui.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemHelpInfoBinding
import com.castprogramms.balamutbatut.tools.HelpInfo
import com.google.android.exoplayer2.ui.SubtitleView

class HelpAdapter : RecyclerView.Adapter<HelpAdapter.HelpViewHolder>() {
    val helpInfos = listOf(
        HelpInfo(R.drawable.help_image_1, "Что такое BatutCoin?"),
        HelpInfo(R.drawable.help_image_2, "Пригласи друга")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        Log.e("data", viewType.toString())
        return HelpViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_help_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        holder.bind(helpInfos[position])
    }

    override fun getItemCount() = helpInfos.size

    inner class HelpViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemHelpInfoBinding.bind(view)
        fun bind(helpInfo: HelpInfo) {
            binding.imageHelp.setImageResource(helpInfo.photo)
            binding.text.text = helpInfo.text
        }
    }
}