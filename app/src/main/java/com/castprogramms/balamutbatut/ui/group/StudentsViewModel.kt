package com.castprogramms.balamutbatut.ui.group

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class StudentsViewModel(private val repository: Repository):ViewModel() {
    fun getStudents(idGroup : String) = repository.getCollectionAllStudent(idGroup)
    fun deleteStudent(studentId: String, groupId: String) =
        repository.deleteStudentFromGroup(studentId, groupId)
    fun getSortedElements() = repository.getAllSortedElements(mapOf())
    fun getStudentElements(studentId: String) = repository.getStudentElements(studentId)
}