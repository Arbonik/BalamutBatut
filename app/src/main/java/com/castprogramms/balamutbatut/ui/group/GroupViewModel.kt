package com.castprogramms.balamutbatut.ui.group


import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.tools.Group

class GroupViewModel(private val repository: Repository) : ViewModel() {
    fun getGroups() = repository.getGroups()
    fun updateDataGroup(group: Group, groupID: String) = repository.updateDataGroup(group, groupID)
}