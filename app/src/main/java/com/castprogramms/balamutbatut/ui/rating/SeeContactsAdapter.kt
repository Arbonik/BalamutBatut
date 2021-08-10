package com.castprogramms.balamutbatut.ui.rating

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.EditContactDialogBinding
import com.castprogramms.balamutbatut.databinding.ItemContactsBinding
import com.castprogramms.balamutbatut.tools.LinkContact
import com.castprogramms.balamutbatut.tools.User

class SeeContactsAdapter : RecyclerView.Adapter<SeeContactsAdapter.SeeContactsViewHolder>() {
    var listLinkContact = mutableListOf<LinkContact>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeContactsViewHolder {
        return SeeContactsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_contacts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SeeContactsViewHolder, position: Int) {
        holder.bind(listLinkContact[position])
    }

    override fun getItemCount() = listLinkContact.size
    inner class SeeContactsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemContactsBinding.bind(view)
        fun bind(linkContact: LinkContact) {
            binding.root.setImageResource(linkContact.img)
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkContact.link))
                itemView.context.startActivity(intent)
            }
        }
    }
}