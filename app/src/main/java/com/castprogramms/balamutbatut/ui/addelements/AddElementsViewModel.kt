package com.castprogramms.balamutbatut.ui.addelements

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.tools.Element

class AddElementsViewModel(private val repository: Repository): ViewModel() {
    fun getAddStudentElementsOnThisTitle(idStudent: String, title: String) =
        repository.getAddStudentElementsOnThisTitle(idStudent, title)

    fun getStudentElementsOnThisTitle(idStudent: String, title: String) =
        repository.getStudentElementsOnThisTitle(idStudent, title)

    fun updateElementsStudent(title: String, elements: MutableList<Element>, idStudent: String) =
        repository.updateElementsStudent(mapOf(title to elements), idStudent)

    fun getStudentElements(idStudent: String) = repository.getStudentElements(idStudent)

    fun getAllElementsOnThisTitle(title: String) = repository.getAllElementsOnThisTitle(title)
}