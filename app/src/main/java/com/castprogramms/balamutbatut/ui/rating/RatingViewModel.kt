package com.castprogramms.balamutbatut.ui.rating

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class RatingViewModel(private val repository: Repository) : ViewModel() {
    fun getAllStudent() = repository.getAllStudents()
    fun getStudentGroups(groupId: String) = repository.getStudentsOfGroup(groupId)
    fun getRang(id: String) = repository.getRang(id)
    fun getGroup(idGroup: String) = repository.getGroup(idGroup)
}