package com.castprogramms.balamutbatut.tools

data class UserContacts(
    var descUser: String = "",
    var listContacts: List<LinkContact> = listOf()
) {
    fun addContact(contact: LinkContact){
        val copy = listContacts.toMutableList()
        if (copy.find { it.img == contact.img} != null)
            copy.remove(copy.find { it.img == contact.img})
        copy.add(contact)
        listContacts = copy.toList()
    }
}