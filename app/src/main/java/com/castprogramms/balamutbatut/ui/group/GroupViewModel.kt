package com.castprogramms.balamutbatut.ui.group


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.model.Group
import com.castprogramms.balamutbatut.model.GroupRepository
import com.castprogramms.balamutbatut.network.Resource

class GroupViewModel(private val repository: Repository) : ViewModel() {
    fun getGroups() = repository.getGroups()
//    val group : LiveData<Resource<Group>> = groupRepository.getGroup()
}