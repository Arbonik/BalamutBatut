package com.castprogramms.balamutbatut.ui.editprofile

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.EditContactDialogBinding
import com.castprogramms.balamutbatut.databinding.ItemContactsBinding
import com.castprogramms.balamutbatut.tools.Contacts
import com.castprogramms.balamutbatut.tools.LinkContact
import com.castprogramms.balamutbatut.tools.User

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    val createLinkContact = mutableListOf<LinkContact>()
    private fun addContact(linkContact: LinkContact) {
        if (linkContact.link != "") {
            if (createLinkContact.find { it.img == linkContact.img } != null)
                createLinkContact.remove(createLinkContact.find { it.img == linkContact.img })
            createLinkContact.add(linkContact)
        }
        else
            if (createLinkContact.find { it.img == linkContact.img } != null)
                createLinkContact.remove(createLinkContact.find { it.img == linkContact.img })
        Log.e("data", createLinkContact.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_contacts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(Contacts.values()[position])
    }

    override fun getItemCount() = Contacts.values().size
    inner class ContactsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userLinks = listOf<LinkContact>()
        init {
            User.mutableLiveDataStudent.observeForever {
                if (it != null){
                    userLinks = it.userContacts.listContacts
                }
            }
        }
        val binding = ItemContactsBinding.bind(view)
        fun bind(contact: Contacts) {
            binding.root.setImageResource(contact.img)
            binding.root.setOnClickListener {
                val alertDialogView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.edit_contact_dialog, null)
                val ad = AlertDialog.Builder(itemView.context)
                    .setView(alertDialogView)
                    .create()
                if (ad.window != null)
                    ad.window!!.setBackgroundDrawable(ColorDrawable(0))

                val alertDialogBinding = EditContactDialogBinding.bind(alertDialogView)
                alertDialogBinding.imageTitle.setImageResource(contact.img)
                val currentLink = createLinkContact.find { it.img == contact.img }
                if (currentLink != null)
                    alertDialogBinding.link.setText(currentLink.link, TextView.BufferType.EDITABLE)
                else{
                    val userLink = userLinks.find { it.img == contact.img }
                    if (userLink != null)
                        alertDialogBinding.link.setText(userLink.link, TextView.BufferType.EDITABLE)
                }

                alertDialogBinding.button.setOnClickListener {
                    if (!alertDialogBinding.link.text.toString()
                            .contains(contact.name.lowercase()) && !alertDialogBinding.link.text?.trim().isNullOrEmpty()
                    ) {
                        alertDialogBinding.linkContainer.error = "Неверная ссылка"
                    } else {
                        addContact(
                            LinkContact(
                                contact.img,
                                alertDialogBinding.link.text.toString().trim()
                            )
                        )
                        ad.dismiss()
                    }
                }

                ad.show()

                alertDialogBinding.link.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(p0: Editable?) {
                        alertDialogBinding.linkContainer.error = null
                    }
                })
            }
        }
    }
}