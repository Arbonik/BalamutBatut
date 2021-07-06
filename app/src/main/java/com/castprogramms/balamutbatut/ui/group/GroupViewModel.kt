package com.castprogramms.balamutbatut.ui.group


import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class GroupViewModel(private val repository: Repository) : ViewModel() {
    fun getGroups() = repository.getGroups()
//    val group : LiveData<Resource<Group>> = groupRepository.getGroup()
}