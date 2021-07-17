package com.castprogramms.balamutbatut.ui.addstudents

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class AddStudentViewModel(private val repository: Repository):ViewModel() {
    fun updateStudentGroup(studentId: String, groupID: String) =
        repository.updateStudentGroup(studentId, groupID)
    fun getStudentWithoutGroup() = repository.getCollectionAllStudentsWithoutGroup()
}